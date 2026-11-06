package com.breckneck.feature.deck.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DeckListRoute

@Serializable
data class DeckFormRoute(
    val deckId: Long? = null,
)
