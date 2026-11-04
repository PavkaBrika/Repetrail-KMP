package com.breckneck.repetrail.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/** Called once by the platform entry point, before creating the UI. */
fun initKoin(platformConfiguration: KoinAppDeclaration): KoinApplication = startKoin {
    modules(commonModule, dataModule)
    platformConfiguration()
}
