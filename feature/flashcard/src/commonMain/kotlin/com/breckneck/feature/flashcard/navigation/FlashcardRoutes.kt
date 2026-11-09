package com.breckneck.feature.flashcard.navigation

import kotlinx.serialization.Serializable

@Serializable
data class DeckDetailRoute(
    val deckId: Long,
)

@Serializable
data class FlashcardFormRoute(
    val deckId: Long,
    val flashcardId: Long? = null,
)
