package com.breckneck.repetrail.di

import androidx.room.RoomDatabase
import com.breckneck.core.common.TimeProvider
import com.breckneck.core.data.local.RepetrailDatabase
import com.breckneck.core.data.local.dao.DeckDao
import com.breckneck.core.data.local.dao.FlashcardDao
import org.koin.core.Koin
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

internal suspend fun assertFoundationGraph(koin: Koin) {
    assertNotNull(koin.get<TimeProvider>())
    assertNotNull(koin.get<RoomDatabase.Builder<RepetrailDatabase>>())

    val database = koin.get<RepetrailDatabase>()
    assertSame(database, koin.get<RepetrailDatabase>())
    assertSame(database.deckDao(), koin.get<DeckDao>())
    assertSame(database.flashcardDao(), koin.get<FlashcardDao>())
    assertSame(koin.get<DeckDao>(), koin.get<DeckDao>())
    assertSame(koin.get<FlashcardDao>(), koin.get<FlashcardDao>())

    // Execute queries: resolving Room's lazy database object alone does not open SQLite.
    assertNull(koin.get<DeckDao>().getById(-1))
    assertNull(koin.get<FlashcardDao>().getById(-1))
}
