package com.breckneck.core.domain.usecase.card

import com.breckneck.core.domain.repository.CardRepository

class ObserveCardsUseCase(private val repository: CardRepository) {
    operator fun invoke(deckId: Long) = repository.observeCards(deckId)
}
