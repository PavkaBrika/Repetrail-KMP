package com.breckneck.feature.flashcard.detail

sealed interface DeckDetailSideEffect {
    data object NavigateBack : DeckDetailSideEffect
    data object NavigateToDeckList : DeckDetailSideEffect
    data class NavigateToDeckForm(val deckId: Long) : DeckDetailSideEffect
    data class NavigateToFlashcardForm(val deckId: Long, val flashcardId: Long? = null) : DeckDetailSideEffect
}
