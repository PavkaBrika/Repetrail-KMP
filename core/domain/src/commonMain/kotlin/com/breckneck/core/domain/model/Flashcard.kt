package com.breckneck.core.domain.model

data class Flashcard(
    val id: Long = 0,
    val deckId: Long,
    val term: String,
    val definition: String,
    val example: String?,
    val createdAt: Long,
)
