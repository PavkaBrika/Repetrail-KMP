package com.breckneck.feature.deck.list

import com.breckneck.core.domain.model.Deck

sealed interface DeckListUiState {
    data object Loading : DeckListUiState
    data class Content(val decks: List<Deck>) : DeckListUiState
    data object Empty : DeckListUiState
    data class Error(val message: String) : DeckListUiState
}

data class DeckListState(
    val uiState: DeckListUiState = DeckListUiState.Loading,
    val pendingDeleteDeck: Deck? = null,
    val deckToDeleteConfirmation: Deck? = null,
)
