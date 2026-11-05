package com.breckneck.core.data.local.model

data class DeckWithCardCount(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val cardCount: Int,
)
