package com.breckneck.feature.deck.form

sealed interface DeckFormIntent {
    data class NameChanged(val name: String) : DeckFormIntent
    data class DescriptionChanged(val description: String) : DeckFormIntent
    data object SaveClicked : DeckFormIntent
    data object BackClicked : DeckFormIntent
    data object ConfirmDiscard : DeckFormIntent
    data object CancelDiscard : DeckFormIntent
}
