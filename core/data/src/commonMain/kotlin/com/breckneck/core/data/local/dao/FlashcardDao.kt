package com.breckneck.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breckneck.core.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert
    suspend fun insert(card: FlashcardEntity): Long

    @Query("SELECT * FROM flashcards WHERE id = :id")
    suspend fun getById(id: Long): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE deckId = :deckId ORDER BY createdAt ASC, id ASC")
    fun observeByDeckId(deckId: Long): Flow<List<FlashcardEntity>>

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteById(id: Long)
}
