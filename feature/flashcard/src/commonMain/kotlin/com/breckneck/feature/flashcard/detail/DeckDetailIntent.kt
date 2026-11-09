package com.breckneck.feature.flashcard.detail

sealed interface DeckDetailIntent {
    data object Retry : DeckDetailIntent
    data object BackClicked : DeckDetailIntent
    data object AddFlashcardClicked : DeckDetailIntent
    data class FlashcardClicked(val flashcardId: Long) : DeckDetailIntent
    data object OpenMenu : DeckDetailIntent
    data object DismissMenu : DeckDetailIntent
    data object EditDeckClicked : DeckDetailIntent
    data object DeleteDeckClicked : DeckDetailIntent
    data object CancelDeleteDeck : DeckDetailIntent
    data object ConfirmDeleteDeck : DeckDetailIntent
}
