package com.breckneck.repetrail.di

import com.breckneck.core.data.local.RepetrailDatabase
import com.breckneck.core.data.local.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import org.koin.dsl.onClose

internal val dataModule = module {
    single { createDatabase(builder = get(), queryDispatcher = Dispatchers.IO) }
        .onClose { it?.close() }
    single { get<RepetrailDatabase>().deckDao() }
    single { get<RepetrailDatabase>().flashcardDao() }
}
