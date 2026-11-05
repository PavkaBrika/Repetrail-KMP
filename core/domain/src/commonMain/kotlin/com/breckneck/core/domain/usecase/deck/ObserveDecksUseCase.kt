package com.breckneck.core.domain.usecase.deck

import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow

class ObserveDecksUseCase(
    private val repository: DeckRepository,
) {
    operator fun invoke(): Flow<List<Deck>> = repository.observeDecks()
}
