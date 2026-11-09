package com.breckneck.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breckneck.core.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert
    suspend fun insert(flashcard: FlashcardEntity): Long

    @Query("SELECT * FROM flashcards WHERE id = :id")
    suspend fun getById(id: Long): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE deckId = :deckId ORDER BY createdAt ASC, id ASC")
    fun observeByDeckId(deckId: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE id = :flashcardId AND deckId = :deckId")
    suspend fun getByIdInDeck(deckId: Long, flashcardId: Long): FlashcardEntity?

    @Query("UPDATE flashcards SET term = :term, definition = :definition, example = :example WHERE id = :flashcardId AND deckId = :deckId")
    suspend fun updateInDeck(deckId: Long, flashcardId: Long, term: String, definition: String, example: String?): Int

    @Query("DELETE FROM flashcards WHERE id = :flashcardId AND deckId = :deckId")
    suspend fun deleteInDeck(deckId: Long, flashcardId: Long): Int

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteById(id: Long)
}
