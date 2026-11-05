package com.breckneck.feature.deck.di

import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.GetDeckByIdUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import com.breckneck.core.domain.usecase.deck.SaveDeckUseCase
import org.koin.dsl.module

val deckModule = module {
    factory { ObserveDecksUseCase(repository = get()) }
    factory { GetDeckByIdUseCase(repository = get()) }
    factory { SaveDeckUseCase(repository = get(), timeProvider = get()) }
    factory { DeleteDeckUseCase(repository = get()) }
}
