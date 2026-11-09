package com.breckneck.core.domain.usecase.flashcard

import com.breckneck.core.domain.repository.FlashcardRepository

class ObserveFlashcardsUseCase(private val repository: FlashcardRepository) {
    operator fun invoke(deckId: Long) = repository.observeFlashcards(deckId)
}
