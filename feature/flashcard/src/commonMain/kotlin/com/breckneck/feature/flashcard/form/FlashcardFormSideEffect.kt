package com.breckneck.feature.flashcard.form

sealed interface FlashcardFormSideEffect {
    data class FlashcardSaved(val isNew: Boolean) : FlashcardFormSideEffect
    data object FlashcardAddedAndContinue : FlashcardFormSideEffect
    data object NavigateBack : FlashcardFormSideEffect
}
