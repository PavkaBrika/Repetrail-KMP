package com.breckneck.core.domain.model

data class Deck(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val cardCount: Int = 0,
    val createdAt: Long,
    val updatedAt: Long,
)
