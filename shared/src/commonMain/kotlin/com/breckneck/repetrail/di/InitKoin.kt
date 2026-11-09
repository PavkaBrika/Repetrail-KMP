package com.breckneck.repetrail.di

import com.breckneck.feature.deck.di.deckModule
import com.breckneck.feature.flashcard.di.flashcardModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/** Called once by the platform entry point, before creating the UI. */
fun initKoin(platformConfiguration: KoinAppDeclaration): KoinApplication = startKoin {
    modules(commonModule, dataModule, deckModule, flashcardModule)
    platformConfiguration()
}
