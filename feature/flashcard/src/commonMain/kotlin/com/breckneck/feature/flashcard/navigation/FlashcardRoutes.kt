package com.breckneck.feature.flashcard.navigation

import kotlinx.serialization.Serializable

const val CARD_FORM_RESULT_KEY = "card_form_result"
const val CARD_FORM_RESULT_CREATED = "created"
const val CARD_FORM_RESULT_UPDATED = "updated"

@Serializable
data class DeckDetailRoute(
    val deckId: Long,
)

@Serializable
data class FlashcardFormRoute(
    val deckId: Long,
    val flashcardId: Long? = null,
)
