package com.breckneck.feature.flashcard.detail

import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.model.Flashcard

sealed interface DeckDetailUiState {
    data object Loading : DeckDetailUiState
    data class Empty(val deck: Deck) : DeckDetailUiState
    data class Content(val deck: Deck, val flashcards: List<Flashcard>) : DeckDetailUiState
    data object Error : DeckDetailUiState
}

data class DeckDetailState(
    val uiState: DeckDetailUiState = DeckDetailUiState.Loading,
    val showMenu: Boolean = false,
    val showDeleteDeckDialog: Boolean = false,
) {
    val deck: Deck?
        get() = when (val ui = uiState) {
            is DeckDetailUiState.Empty -> ui.deck
            is DeckDetailUiState.Content -> ui.deck
            DeckDetailUiState.Loading, DeckDetailUiState.Error -> null
        }
}
