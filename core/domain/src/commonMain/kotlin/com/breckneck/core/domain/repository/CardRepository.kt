package com.breckneck.core.domain.repository

import com.breckneck.core.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun observeCards(deckId: Long): Flow<List<Card>>
    suspend fun getCardById(deckId: Long, cardId: Long): Card?
    suspend fun createCard(card: Card): Long
    suspend fun updateCard(card: Card): Boolean
    suspend fun deleteCard(deckId: Long, cardId: Long): Boolean
}
