package com.breckneck.core.domain.usecase.deck

import com.breckneck.core.common.TimeProvider
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.repository.DeckRepository

class SaveDeckUseCase(
    private val repository: DeckRepository,
    private val timeProvider: TimeProvider,
) {
    suspend operator fun invoke(
        id: Long = 0,
        name: String,
        description: String?,
        createdAt: Long? = null,
    ): Long {
        val now = timeProvider.currentTimeMillis()
        val existingDeck = if (id != 0L) repository.getDeckById(id) else null
        val actualCreatedAt = createdAt ?: existingDeck?.createdAt ?: now
        val deck = Deck(
            id = id,
            name = name,
            description = description,
            createdAt = actualCreatedAt,
            updatedAt = now,
        )
        return repository.saveDeck(deck)
    }
}
