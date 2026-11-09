package com.breckneck.core.domain.repository

import com.breckneck.core.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun observeFlashcards(deckId: Long): Flow<List<Flashcard>>
    suspend fun getFlashcardById(deckId: Long, flashcardId: Long): Flashcard?
    suspend fun createFlashcard(flashcard: Flashcard): Long
    suspend fun updateFlashcard(flashcard: Flashcard): Boolean
    suspend fun deleteFlashcard(deckId: Long, flashcardId: Long): Boolean
}
