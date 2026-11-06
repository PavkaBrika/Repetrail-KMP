package com.breckneck.feature.deck.form

sealed interface DeckFormSideEffect {
    data class DeckSaved(val deckName: String, val isNew: Boolean) : DeckFormSideEffect
    data object NavigateBack : DeckFormSideEffect
}
