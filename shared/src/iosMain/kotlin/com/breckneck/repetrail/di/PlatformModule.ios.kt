package com.breckneck.repetrail.di

import androidx.room.RoomDatabase
import com.breckneck.core.data.local.RepetrailDatabase
import com.breckneck.core.data.local.createDatabaseBuilder
import org.koin.dsl.module

internal val iosPlatformModule = module {
    single<RoomDatabase.Builder<RepetrailDatabase>> { createDatabaseBuilder() }
}

private val iosKoin by lazy {
    initKoin {
        modules(iosPlatformModule)
    }
}

/** Safe to call again when the hosting UIViewController is recreated. */
fun initKoinIos() {
    iosKoin
}
