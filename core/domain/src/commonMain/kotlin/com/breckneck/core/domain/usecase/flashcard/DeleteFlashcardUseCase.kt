package com.breckneck.core.domain.usecase.flashcard

import com.breckneck.core.domain.repository.FlashcardRepository

class DeleteFlashcardUseCase(private val repository: FlashcardRepository) {
    suspend operator fun invoke(deckId: Long, flashcardId: Long) = repository.deleteFlashcard(deckId, flashcardId)
}
