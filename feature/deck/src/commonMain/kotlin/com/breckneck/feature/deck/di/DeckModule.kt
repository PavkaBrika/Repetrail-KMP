package com.breckneck.feature.deck.di

import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.GetDeckByIdUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import com.breckneck.core.domain.usecase.deck.SaveDeckUseCase
import com.breckneck.feature.deck.form.DeckFormViewModel
import com.breckneck.feature.deck.list.DeckListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val deckModule = module {
    factory { ObserveDecksUseCase(repository = get()) }
    factory { GetDeckByIdUseCase(repository = get()) }
    factory { SaveDeckUseCase(repository = get(), timeProvider = get()) }
    factory { DeleteDeckUseCase(repository = get()) }
    viewModel {
        DeckListViewModel(
            observeDecksUseCase = get(),
            deleteDeckUseCase = get(),
        )
    }
    viewModel {
        DeckFormViewModel(
            savedStateHandle = get(),
            getDeckByIdUseCase = get(),
            saveDeckUseCase = get(),
        )
    }
}
