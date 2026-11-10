package com.breckneck.feature.flashcard.form.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.breckneck.feature.flashcard.form.FlashcardFormIntent
import com.breckneck.feature.flashcard.form.FlashcardFormSideEffect
import com.breckneck.feature.flashcard.form.FlashcardFormUiState
import com.breckneck.feature.flashcard.form.FlashcardFormViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import repetrail.feature.flashcard.generated.resources.Res
import repetrail.feature.flashcard.generated.resources.flashcard_form_added_snackbar

@Composable
internal fun FlashcardFormScreen(
    viewModel: FlashcardFormViewModel,
    onNavigateBack: (isNew: Boolean?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val keyboardController = LocalSoftwareKeyboardController.current
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val navigationState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)

    val focusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    NavigationBackHandler(
        state = navigationState,
        isBackEnabled = (!state.showDiscardDialog) && (
            isImeVisible ||
            state.hasUnsavedChanges ||
            (state.uiState == FlashcardFormUiState.Saving)
        ),
        onBackCompleted = {
            if (isImeVisible) {
                keyboardController?.hide()
            } else if (state.uiState != FlashcardFormUiState.Saving) {
                viewModel.onIntent(FlashcardFormIntent.BackClicked)
            }
        },
    )

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is FlashcardFormSideEffect.FlashcardSaved -> onNavigateBack(effect.isNew)

                    FlashcardFormSideEffect.NavigateBack -> onNavigateBack(null)

                    FlashcardFormSideEffect.FlashcardAddedAndContinue -> {
                        coroutineScope.launch {
                            val message = getString(Res.string.flashcard_form_added_snackbar)
                            snackbarHostState.showSnackbar(message)
                        }
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }

    FlashcardFormContent(
        state = state,
        onIntent = viewModel::onIntent,
        focusRequester = focusRequester,
        snackbarHostState = snackbarHostState,
    )
}
