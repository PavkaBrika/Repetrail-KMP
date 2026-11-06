package com.breckneck.feature.deck.di

import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.GetDeckByIdUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import com.breckneck.core.domain.usecase.deck.SaveDeckUseCase
import com.breckneck.feature.deck.form.DeckFormScreenModel
import com.breckneck.feature.deck.list.DeckListScreenModel
import org.koin.dsl.module

val deckModule = module {
    factory { ObserveDecksUseCase(repository = get()) }
    factory { GetDeckByIdUseCase(repository = get()) }
    factory { SaveDeckUseCase(repository = get(), timeProvider = get()) }
    factory { DeleteDeckUseCase(repository = get()) }
    factory { DeckListScreenModel(observeDecksUseCase = get(), deleteDeckUseCase = get()) }
    factory { (deckId: Long?) ->
        DeckFormScreenModel(
            deckId = deckId,
            getDeckByIdUseCase = get(),
            saveDeckUseCase = get(),
        )
    }
}
