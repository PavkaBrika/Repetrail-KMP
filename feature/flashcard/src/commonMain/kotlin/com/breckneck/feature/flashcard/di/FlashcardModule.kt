package com.breckneck.feature.flashcard.di

import com.breckneck.core.domain.usecase.flashcard.DeleteFlashcardUseCase
import com.breckneck.core.domain.usecase.flashcard.GetFlashcardByIdUseCase
import com.breckneck.core.domain.usecase.flashcard.ObserveFlashcardsUseCase
import com.breckneck.core.domain.usecase.flashcard.SaveFlashcardUseCase
import com.breckneck.feature.flashcard.detail.DeckDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val flashcardModule = module {
    factory { ObserveFlashcardsUseCase(repository = get()) }
    factory { GetFlashcardByIdUseCase(repository = get()) }
    factory { SaveFlashcardUseCase(flashcardRepository = get(), timeProvider = get()) }
    factory { DeleteFlashcardUseCase(repository = get()) }
    viewModel {
        DeckDetailViewModel(
            savedStateHandle = get(),
            observeDeckByIdUseCase = get(),
            observeFlashcardsUseCase = get(),
            deleteDeckUseCase = get(),
        )
    }
}
