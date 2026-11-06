package com.breckneck.feature.deck.form

sealed interface DeckFormUiState {
    data object Loading : DeckFormUiState
    data object Content : DeckFormUiState
    data object Saving : DeckFormUiState
}

data class DeckFormState(
    val deckId: Long? = null,
    val uiState: DeckFormUiState = DeckFormUiState.Content,
    val name: String = "",
    val description: String = "",
    val nameError: String? = null,
    val originalName: String = "",
    val originalDescription: String = "",
    val showDiscardDialog: Boolean = false,
) {
    val isEditMode: Boolean get() = deckId != null
    val hasUnsavedChanges: Boolean get() = name != originalName || description != originalDescription
}
