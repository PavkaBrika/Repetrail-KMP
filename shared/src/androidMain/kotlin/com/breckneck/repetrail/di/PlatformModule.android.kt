package com.breckneck.repetrail.di

import android.content.Context
import androidx.room.RoomDatabase
import com.breckneck.core.data.local.RepetrailDatabase
import com.breckneck.core.data.local.createDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val androidPlatformModule = module {
    single<RoomDatabase.Builder<RepetrailDatabase>> {
        createDatabaseBuilder(androidContext())
    }
}

/** The Application owns initialization; Activity recreation does not call this. */
fun initKoinAndroid(context: Context) {
    initKoin {
        androidContext(context.applicationContext)
        modules(androidPlatformModule)
    }
}
