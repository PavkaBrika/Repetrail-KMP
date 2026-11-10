package com.breckneck.feature.flashcard.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDeckByIdUseCase
import com.breckneck.core.domain.usecase.flashcard.DeleteFlashcardUseCase
import com.breckneck.core.domain.usecase.flashcard.ObserveFlashcardsUseCase
import com.breckneck.feature.flashcard.navigation.DeckDetailRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val observeDeckByIdUseCase: ObserveDeckByIdUseCase,
    private val observeFlashcardsUseCase: ObserveFlashcardsUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val deleteFlashcardUseCase: DeleteFlashcardUseCase,
) : ViewModel() {

    private val deckId = savedStateHandle.toRoute<DeckDetailRoute>().deckId
    private val mutableState = MutableStateFlow(DeckDetailState())
    val state = mutableState.asStateFlow()

    private val effects = Channel<DeckDetailSideEffect>(Channel.BUFFERED)
    val sideEffect = effects.receiveAsFlow()

    private var observeJob: Job? = null

    init {
        observeDeck()
    }

    fun onIntent(intent: DeckDetailIntent) {
        when (intent) {
            DeckDetailIntent.Retry -> observeDeck()
            DeckDetailIntent.BackClicked -> effects.trySend(DeckDetailSideEffect.NavigateBack)
            DeckDetailIntent.AddFlashcardClicked ->
                effects.trySend(DeckDetailSideEffect.NavigateToFlashcardForm(deckId))

            is DeckDetailIntent.FlashcardClicked ->
                effects.trySend(
                    DeckDetailSideEffect.NavigateToFlashcardForm(
                        deckId,
                        intent.flashcardId
                    )
                )

            is DeckDetailIntent.DeleteFlashcardClicked -> deleteFlashcard(intent.flashcardId)

            DeckDetailIntent.OpenMenu -> mutableState.update { it.copy(showMenu = true) }

            DeckDetailIntent.DismissMenu -> mutableState.update { it.copy(showMenu = false) }
            DeckDetailIntent.EditDeckClicked -> {
                mutableState.update { it.copy(showMenu = false) }
                effects.trySend(DeckDetailSideEffect.NavigateToDeckForm(deckId))
            }

            DeckDetailIntent.DeleteDeckClicked -> mutableState.update {
                it.copy(showMenu = false, showDeleteDeckDialog = true)
            }

            DeckDetailIntent.CancelDeleteDeck -> mutableState.update {
                it.copy(showDeleteDeckDialog = false)
            }

            DeckDetailIntent.ConfirmDeleteDeck -> deleteDeck()
        }
    }

    private fun observeDeck() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            combine(
                observeDeckByIdUseCase(deckId),
                observeFlashcardsUseCase(deckId),
            ) { deck, flashcards ->
                deck to flashcards
            }.collect { (deck, flashcards) ->
                val currentDeck = deck ?: return@collect
                val uiState = if (flashcards.isEmpty()) {
                    DeckDetailUiState.Empty(currentDeck)
                } else {
                    DeckDetailUiState.Content(currentDeck, flashcards)
                }
                mutableState.update { it.copy(uiState = uiState) }
            }
        }
    }

    private fun deleteFlashcard(flashcardId: Long) {
        viewModelScope.launch {
            deleteFlashcardUseCase(deckId, flashcardId)
        }
    }

    private fun deleteDeck() {
        mutableState.update { it.copy(showDeleteDeckDialog = false) }
        viewModelScope.launch {
            deleteDeckUseCase(deckId)
            effects.trySend(DeckDetailSideEffect.NavigateToDeckList)
        }
    }
}
