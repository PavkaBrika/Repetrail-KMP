package com.breckneck.core.domain.usecase.card

import com.breckneck.core.common.TimeProvider
import com.breckneck.core.domain.model.Card
import com.breckneck.core.domain.repository.CardRepository

class SaveCardUseCase(
    private val cardRepository: CardRepository,
    private val timeProvider: TimeProvider,
) {
    suspend operator fun invoke(
        deckId: Long,
        cardId: Long? = null,
        term: String,
        definition: String,
        example: String?,
    ): Long? {
        val trimmedTerm = term.trim()
        val trimmedDefinition = definition.trim()
        val trimmedExample = example?.trim()?.ifBlank { null }
        if (cardId == null) {
            return cardRepository.createCard(
                Card(deckId = deckId, term = trimmedTerm, definition = trimmedDefinition,
                    example = trimmedExample, createdAt = timeProvider.currentTimeMillis())
            )
        }
        val existing = cardRepository.getCardById(deckId, cardId) ?: return null
        val updated = existing.copy(term = trimmedTerm, definition = trimmedDefinition, example = trimmedExample)
        if (!cardRepository.updateCard(updated)) return null
        return cardId
    }
}
