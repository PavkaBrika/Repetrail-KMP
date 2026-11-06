package com.breckneck.feature.deck.list

import com.breckneck.core.domain.model.Deck

sealed interface DeckListIntent {
    data object Retry : DeckListIntent
    data class DeckClicked(val deckId: Long) : DeckListIntent
    data object CreateDeckClicked : DeckListIntent
    data class EditDeckClicked(val deckId: Long) : DeckListIntent
    data class DeleteDeckClicked(val deck: Deck) : DeckListIntent
    data object ConfirmDelete : DeckListIntent
    data object CancelDelete : DeckListIntent
}
