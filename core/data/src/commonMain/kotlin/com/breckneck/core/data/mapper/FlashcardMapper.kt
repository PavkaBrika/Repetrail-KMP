package com.breckneck.core.data.mapper

import com.breckneck.core.data.local.entity.FlashcardEntity
import com.breckneck.core.domain.model.Flashcard

fun FlashcardEntity.toDomain() = Flashcard(id, deckId, term, definition, example, createdAt)

fun Flashcard.toEntity() = FlashcardEntity(id, deckId, term, definition, example, createdAt)
