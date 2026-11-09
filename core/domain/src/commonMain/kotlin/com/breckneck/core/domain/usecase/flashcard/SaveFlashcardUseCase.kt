package com.breckneck.core.domain.usecase.flashcard

import com.breckneck.core.common.TimeProvider
import com.breckneck.core.domain.model.Flashcard
import com.breckneck.core.domain.repository.FlashcardRepository

class SaveFlashcardUseCase(
    private val flashcardRepository: FlashcardRepository,
    private val timeProvider: TimeProvider,
) {
    suspend operator fun invoke(
        deckId: Long,
        flashcardId: Long? = null,
        term: String,
        definition: String,
        example: String?,
    ): Long? {
        val trimmedTerm = term.trim()
        val trimmedDefinition = definition.trim()
        val trimmedExample = example?.trim()?.ifBlank { null }
        if (flashcardId == null) {
            return flashcardRepository.createFlashcard(
                Flashcard(deckId = deckId, term = trimmedTerm, definition = trimmedDefinition,
                    example = trimmedExample, createdAt = timeProvider.currentTimeMillis())
            )
        }
        val existing = flashcardRepository.getFlashcardById(deckId, flashcardId) ?: return null
        val updated = existing.copy(term = trimmedTerm, definition = trimmedDefinition, example = trimmedExample)
        if (!flashcardRepository.updateFlashcard(updated)) return null
        return flashcardId
    }
}
