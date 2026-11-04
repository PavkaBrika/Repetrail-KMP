package com.breckneck.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun createDatabaseBuilder(context: Context): RoomDatabase.Builder<RepetrailDatabase> {
    val appContext = context.applicationContext
    return Room.databaseBuilder<RepetrailDatabase>(
        context = appContext,
        name = appContext.getDatabasePath(DATABASE_NAME).absolutePath,
    )
}
