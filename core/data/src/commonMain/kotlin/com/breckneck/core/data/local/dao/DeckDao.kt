package com.breckneck.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.breckneck.core.data.local.entity.DeckEntity
import com.breckneck.core.data.local.model.DeckWithCardCount
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insert(deck: DeckEntity): Long

    @Update
    suspend fun update(deck: DeckEntity)

    @Query("SELECT * FROM decks WHERE id = :id")
    suspend fun getById(id: Long): DeckEntity?

    @Query("SELECT * FROM decks ORDER BY updatedAt DESC, id DESC")
    fun observeAll(): Flow<List<DeckEntity>>

    @Query("""
        SELECT d.id, d.name, d.description, d.createdAt, d.updatedAt, COUNT(f.id) AS cardCount
        FROM decks d
        LEFT JOIN flashcards f ON d.id = f.deckId
        GROUP BY d.id
        ORDER BY d.updatedAt DESC, d.id DESC
    """)
    fun observeAllWithCardCount(): Flow<List<DeckWithCardCount>>

    @Query("DELETE FROM decks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM flashcards WHERE deckId = :deckId")
    suspend fun getFlashcardCount(deckId: Long): Int
}
