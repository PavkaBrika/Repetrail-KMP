package com.breckneck.repetrail.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.breckneck.core.data.local.RepetrailDatabase
import kotlinx.coroutines.test.runTest
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class FoundationGraphAndroidTest {
    @Test
    fun resolvesDatabaseAndDaos() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val application = koinApplication {
            androidContext(context)
            modules(commonModule, dataModule, androidPlatformModule)
        }
        try {
            // Resolve the actual platform definition, without opening the user's database.
            assertNotNull(application.koin.get<RoomDatabase.Builder<RepetrailDatabase>>())
            application.koin.loadModules(listOf(module {
                single<RoomDatabase.Builder<RepetrailDatabase>> {
                    Room.inMemoryDatabaseBuilder<RepetrailDatabase>(context)
                }
            }))
            assertFoundationGraph(application.koin)
        } finally {
            application.close()
        }
    }
}
