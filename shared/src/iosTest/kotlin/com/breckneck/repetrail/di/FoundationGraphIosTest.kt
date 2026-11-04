package com.breckneck.repetrail.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.breckneck.core.data.local.RepetrailDatabase
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class FoundationGraphIosTest {
    @Test
    fun resolvesDatabaseAndDaos() = runTest {
        val application = koinApplication {
            modules(commonModule, dataModule, iosPlatformModule)
        }
        try {
            // Resolve the actual platform definition, without opening the user's database.
            assertNotNull(application.koin.get<RoomDatabase.Builder<RepetrailDatabase>>())
            application.koin.loadModules(listOf(module {
                single<RoomDatabase.Builder<RepetrailDatabase>> {
                    Room.inMemoryDatabaseBuilder<RepetrailDatabase>()
                }
            }))
            assertFoundationGraph(application.koin)
        } finally {
            application.close()
        }
    }
}
