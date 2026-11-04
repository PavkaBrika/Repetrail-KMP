package com.breckneck.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Timestamps are Unix time in milliseconds. */
@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
