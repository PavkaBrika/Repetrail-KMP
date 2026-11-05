package com.breckneck.feature.deck.presentation

import com.breckneck.core.domain.model.Deck

data class DeleteDeckDialogState(
    val isVisible: Boolean,
    val deckToDelete: Deck?,
) {
    companion object {
        fun initial() = DeleteDeckDialogState(
            isVisible = false,
            deckToDelete = null
        )
    }
}
