package com.breckneck.feature.deck.presentation

import com.breckneck.core.domain.model.Deck

sealed interface DeckListUiState {
    data object Loading : DeckListUiState
    data class Content(val decks: List<Deck>) : DeckListUiState
    data object Empty : DeckListUiState
    data class Error(val message: String) : DeckListUiState
}

data class DeckListState(
    val uiState: DeckListUiState,
    val deckAwaitingDeletion: Deck?,
    val deleteDeckDialogState: DeleteDeckDialogState,
) {
    companion object {
        fun initial() = DeckListState(
            uiState = DeckListUiState.Loading,
            deckAwaitingDeletion = null,
            deleteDeckDialogState = DeleteDeckDialogState.initial()
        )
    }
}
