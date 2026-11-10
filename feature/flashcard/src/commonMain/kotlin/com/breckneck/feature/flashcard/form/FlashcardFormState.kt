package com.breckneck.feature.flashcard.form

sealed interface FlashcardFormUiState {
    data object Loading : FlashcardFormUiState
    data object Content : FlashcardFormUiState
    data object Saving : FlashcardFormUiState
}

data class FlashcardFormState(
    val deckId: Long = 0L,
    val flashcardId: Long? = null,
    val uiState: FlashcardFormUiState = FlashcardFormUiState.Content,
    val term: String = "",
    val definition: String = "",
    val example: String = "",
    val termError: String? = null,
    val definitionError: String? = null,
    val originalTerm: String = "",
    val originalDefinition: String = "",
    val originalExample: String = "",
    val showDiscardDialog: Boolean = false,
) {
    val isEditMode: Boolean get() = flashcardId != null
    val hasUnsavedChanges: Boolean
        get() = term != originalTerm || definition != originalDefinition || example != originalExample
}
