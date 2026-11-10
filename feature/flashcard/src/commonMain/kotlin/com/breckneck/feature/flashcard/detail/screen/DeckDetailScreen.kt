package com.breckneck.feature.flashcard.detail.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.breckneck.feature.flashcard.detail.DeckDetailSideEffect
import com.breckneck.feature.flashcard.detail.DeckDetailViewModel

@Composable
internal fun DeckDetailScreen(
    viewModel: DeckDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDeckList: () -> Unit,
    onEditDeck: (Long) -> Unit,
    onOpenFlashcardForm: (Long, Long?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

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
    )
}
