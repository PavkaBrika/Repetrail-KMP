package com.breckneck.core.domain.repository

import com.breckneck.core.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    fun observeDecks(): Flow<List<Deck>>
    suspend fun getDeckById(id: Long): Deck?
    suspend fun saveDeck(deck: Deck): Long
    suspend fun deleteDeck(id: Long)
    suspend fun getFlashcardCount(deckId: Long): Int
}
