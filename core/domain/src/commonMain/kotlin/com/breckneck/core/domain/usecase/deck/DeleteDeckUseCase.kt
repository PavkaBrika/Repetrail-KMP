package com.breckneck.core.domain.usecase.deck

import com.breckneck.core.domain.repository.DeckRepository

class DeleteDeckUseCase(
    private val repository: DeckRepository,
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteDeck(id)
    }
}
