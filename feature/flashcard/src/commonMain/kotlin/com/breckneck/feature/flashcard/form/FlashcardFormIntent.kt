package com.breckneck.feature.flashcard.form

sealed interface FlashcardFormIntent {
    data class TermChanged(val term: String) : FlashcardFormIntent
    data class DefinitionChanged(val definition: String) : FlashcardFormIntent
    data class ExampleChanged(val example: String) : FlashcardFormIntent
    data object SaveClicked : FlashcardFormIntent
    data object SaveAndAddAnotherClicked : FlashcardFormIntent
    data object BackClicked : FlashcardFormIntent
    data object ConfirmDiscard : FlashcardFormIntent
    data object CancelDiscard : FlashcardFormIntent
}
