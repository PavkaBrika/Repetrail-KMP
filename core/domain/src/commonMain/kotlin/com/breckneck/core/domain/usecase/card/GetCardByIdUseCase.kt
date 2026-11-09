package com.breckneck.core.domain.usecase.card

import com.breckneck.core.domain.repository.CardRepository

class GetCardByIdUseCase(private val repository: CardRepository) {
    suspend operator fun invoke(deckId: Long, cardId: Long) = repository.getCardById(deckId, cardId)
}
