package com.breckneck.core.domain.usecase.deck

import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.repository.DeckRepository

class GetDeckByIdUseCase(
    private val repository: DeckRepository,
) {
    suspend operator fun invoke(id: Long): Deck? = repository.getDeckById(id)
}
