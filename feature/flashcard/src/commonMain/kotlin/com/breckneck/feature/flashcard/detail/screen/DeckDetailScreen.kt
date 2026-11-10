package com.breckneck.feature.flashcard.detail.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import com.breckneck.feature.flashcard.detail.DeckDetailSideEffect
import com.breckneck.feature.flashcard.detail.DeckDetailViewModel
import com.breckneck.feature.flashcard.navigation.CARD_FORM_RESULT_CREATED
import com.breckneck.feature.flashcard.navigation.CARD_FORM_RESULT_KEY
import com.breckneck.feature.flashcard.navigation.CARD_FORM_RESULT_UPDATED
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import repetrail.feature.flashcard.generated.resources.Res
import repetrail.feature.flashcard.generated.resources.flashcard_form_added_snackbar
import repetrail.feature.flashcard.generated.resources.flashcard_form_saved_snackbar

@Composable
internal fun DeckDetailScreen(
    viewModel: DeckDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDeckList: () -> Unit,
    onEditDeck: (Long) -> Unit,
    onOpenFlashcardForm: (Long, Long?) -> Unit,
    backStackEntry: NavBackStackEntry? = null,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(backStackEntry, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            val savedStateHandle = backStackEntry?.savedStateHandle
            val result = savedStateHandle?.get<String>(CARD_FORM_RESULT_KEY)
            if (result != null) {
                savedStateHandle.remove<String>(CARD_FORM_RESULT_KEY)
                val message = when (result) {
                    CARD_FORM_RESULT_CREATED -> getString(Res.string.flashcard_form_added_snackbar)
                    CARD_FORM_RESULT_UPDATED -> getString(Res.string.flashcard_form_saved_snackbar)
                    else -> null
                }
                if (message != null) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    DeckDetailSideEffect.NavigateBack -> onNavigateBack()
                    DeckDetailSideEffect.NavigateToDeckList -> onNavigateToDeckList()
                    is DeckDetailSideEffect.NavigateToDeckForm -> onEditDeck(effect.deckId)
                    is DeckDetailSideEffect.NavigateToFlashcardForm ->
                        onOpenFlashcardForm(effect.deckId, effect.flashcardId)
                }
            }
        }
    }

    DeckDetailContent(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
    )
}
