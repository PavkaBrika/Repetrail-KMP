package com.breckneck.feature.deck.form.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.breckneck.feature.deck.form.DeckFormIntent
import com.breckneck.feature.deck.form.DeckFormSideEffect
import com.breckneck.feature.deck.form.DeckFormViewModel

@Composable
internal fun DeckFormScreen(
    viewModel: DeckFormViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val keyboardController = LocalSoftwareKeyboardController.current
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val navigationState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)

    NavigationBackHandler(
        state = navigationState,
        isBackEnabled = !state.showDiscardDialog && (isImeVisible || state.hasUnsavedChanges),
        onBackCompleted = {
            if (isImeVisible) {
                keyboardController?.hide()
            } else {
                viewModel.onIntent(DeckFormIntent.BackClicked)
            }
        },
    )

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckFormSideEffect.DeckSaved -> {
                        // Handled by NavigateBack
                    }

                    DeckFormSideEffect.NavigateBack -> {
                        onNavigateBack()
                    }
                }
            }
        }
    }

    DeckFormContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
