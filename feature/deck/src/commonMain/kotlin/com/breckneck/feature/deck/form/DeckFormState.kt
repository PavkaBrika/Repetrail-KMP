package com.breckneck.feature.deck.form

data class DeckFormState(
    val deckId: Long? = null,
    val name: String = "",
    val description: String = "",
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val originalName: String = "",
    val originalDescription: String = "",
    val showDiscardDialog: Boolean = false,
) {
    val isEditMode: Boolean get() = deckId != null
    val hasUnsavedChanges: Boolean get() = name != originalName || description != originalDescription
}
