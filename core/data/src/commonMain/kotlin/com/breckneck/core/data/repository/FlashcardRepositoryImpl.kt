package com.breckneck.core.data.repository

import com.breckneck.core.data.local.dao.FlashcardDao
import com.breckneck.core.data.mapper.toDomain
import com.breckneck.core.data.mapper.toEntity
import com.breckneck.core.domain.model.Flashcard
import com.breckneck.core.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.map

class FlashcardRepositoryImpl(private val flashcardDao: FlashcardDao) : FlashcardRepository {
    override fun observeFlashcards(deckId: Long) =
        flashcardDao.observeByDeckId(deckId).map { flashcards -> flashcards.map { it.toDomain() } }

    override suspend fun getFlashcardById(deckId: Long, flashcardId: Long): Flashcard? =
        flashcardDao.getByIdInDeck(deckId, flashcardId)?.toDomain()

    override suspend fun createFlashcard(flashcard: Flashcard): Long = flashcardDao.insert(flashcard.toEntity())

    override suspend fun updateFlashcard(flashcard: Flashcard): Boolean =
        flashcardDao.updateInDeck(flashcard.deckId, flashcard.id, flashcard.term, flashcard.definition, flashcard.example) == 1

    override suspend fun deleteFlashcard(deckId: Long, flashcardId: Long): Boolean =
        flashcardDao.deleteInDeck(deckId, flashcardId) == 1
}
