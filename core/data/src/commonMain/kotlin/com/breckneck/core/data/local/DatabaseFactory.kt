package com.breckneck.core.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineDispatcher

internal const val DATABASE_NAME = "repetrail.db"

fun createDatabase(
    builder: RoomDatabase.Builder<RepetrailDatabase>,
    queryDispatcher: CoroutineDispatcher,
): RepetrailDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(queryDispatcher)
    .build()
