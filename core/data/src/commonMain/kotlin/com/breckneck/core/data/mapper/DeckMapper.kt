package com.breckneck.core.data.mapper

import com.breckneck.core.data.local.entity.DeckEntity
import com.breckneck.core.data.local.model.DeckWithCardCount
import com.breckneck.core.domain.model.Deck

fun DeckWithCardCount.toDomain(): Deck = Deck(
    id = id,
    name = name,
    description = description,
    cardCount = cardCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun DeckEntity.toDomain(cardCount: Int = 0): Deck = Deck(
    id = id,
    name = name,
    description = description,
    cardCount = cardCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Deck.toEntity(): DeckEntity = DeckEntity(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
