package com.breckneck.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.breckneck.core.data.local.entity.DeckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insert(deck: DeckEntity): Long

    @Query("SELECT * FROM decks WHERE id = :id")
    suspend fun getById(id: Long): DeckEntity?

    @Query("SELECT * FROM decks ORDER BY createdAt DESC, id DESC")
    fun observeAll(): Flow<List<DeckEntity>>

    @Query("DELETE FROM decks WHERE id = :id")
    suspend fun deleteById(id: Long)
}
