package com.breckneck.feature.flashcard.detail

import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.model.Flashcard

sealed interface DeckDetailUiState {
    data object Loading : DeckDetailUiState
    data class Content(val deck: Deck, val flashcards: List<Flashcard>) : DeckDetailUiState
    data object NotFound : DeckDetailUiState
    data object Error : DeckDetailUiState
}

enum class DeckDetailOperationError {
    DeleteDeck,
}

data class DeckDetailState(
    val uiState: DeckDetailUiState = DeckDetailUiState.Loading,
    val showMenu: Boolean = false,
    val showDeleteDeckDialog: Boolean = false,
    val isMutating: Boolean = false,
    val operationError: DeckDetailOperationError? = null,
)
