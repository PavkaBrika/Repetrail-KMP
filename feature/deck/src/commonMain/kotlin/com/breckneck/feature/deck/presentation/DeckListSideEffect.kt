package com.breckneck.feature.deck.presentation

sealed interface DeckListSideEffect {
    data class NavigateToDeckForm(val deckId: Long? = null) : DeckListSideEffect
    data class NavigateToDeckDetail(val deckId: Long) : DeckListSideEffect
    data class ShowUndoSnackbar(val deckName: String) : DeckListSideEffect
}
