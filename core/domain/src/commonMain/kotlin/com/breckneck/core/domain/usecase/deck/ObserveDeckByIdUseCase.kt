package com.breckneck.core.domain.usecase.deck

import com.breckneck.core.domain.repository.DeckRepository

class ObserveDeckByIdUseCase(private val repository: DeckRepository) {
    operator fun invoke(id: Long) = repository.observeDeckById(id)
}
