package com.breckneck.core.domain.usecase.card

import com.breckneck.core.domain.repository.CardRepository

class DeleteCardUseCase(private val repository: CardRepository) {
    suspend operator fun invoke(deckId: Long, cardId: Long) = repository.deleteCard(deckId, cardId)
}
