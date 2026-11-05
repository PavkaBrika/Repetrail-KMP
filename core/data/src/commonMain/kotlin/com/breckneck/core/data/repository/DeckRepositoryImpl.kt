package com.breckneck.core.data.repository

import com.breckneck.core.data.local.dao.DeckDao
import com.breckneck.core.data.mapper.toDomain
import com.breckneck.core.data.mapper.toEntity
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeckRepositoryImpl(
    private val deckDao: DeckDao,
) : DeckRepository {

    override fun observeDecks(): Flow<List<Deck>> {
        return deckDao.observeAllWithCardCount().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getDeckById(id: Long): Deck? {
        val entity = deckDao.getById(id) ?: return null
        val cardCount = deckDao.getFlashcardCount(id)
        return entity.toDomain(cardCount = cardCount)
    }

    override suspend fun saveDeck(deck: Deck): Long {
        val entity = deck.toEntity()
        return if (deck.id == 0L) {
            deckDao.insert(entity)
        } else {
            deckDao.update(entity)
            deck.id
        }
    }

    override suspend fun deleteDeck(id: Long) {
        deckDao.deleteById(id)
    }

    override suspend fun getFlashcardCount(deckId: Long): Int {
        return deckDao.getFlashcardCount(deckId)
    }
}
