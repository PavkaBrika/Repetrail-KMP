package com.breckneck.feature.flashcard.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDeckByIdUseCase
import com.breckneck.core.domain.usecase.flashcard.ObserveFlashcardsUseCase
import com.breckneck.feature.flashcard.navigation.DeckDetailRoute
import kotlinx.coroutines.CancellationException
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
) : ViewModel() {

    private val deckId = savedStateHandle.toRoute<DeckDetailRoute>().deckId
    private val mutableState = MutableStateFlow(DeckDetailState())
    val state = mutableState.asStateFlow()

    private val effects = Channel<DeckDetailSideEffect>(Channel.BUFFERED)
    val sideEffect = effects.receiveAsFlow()

    private var observeJob: Job? = null
    private var deletingDeck = false

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

            DeckDetailIntent.OpenMenu -> {
                if (state.value.uiState is DeckDetailUiState.Content && !state.value.isMutating) {
                    mutableState.update { it.copy(showMenu = true) }
                }
            }

            DeckDetailIntent.DismissMenu -> mutableState.update { it.copy(showMenu = false) }
            DeckDetailIntent.EditDeckClicked -> {
                mutableState.update { it.copy(showMenu = false) }
                effects.trySend(DeckDetailSideEffect.NavigateToDeckForm(deckId))
            }

            DeckDetailIntent.DeleteDeckClicked -> {
                if (state.value.uiState is DeckDetailUiState.Content && !state.value.isMutating) {
                    mutableState.update { it.copy(showMenu = false, showDeleteDeckDialog = true) }
                }
            }

            DeckDetailIntent.CancelDeleteDeck -> mutableState.update {
                it.copy(showDeleteDeckDialog = false)
            }

            DeckDetailIntent.ConfirmDeleteDeck -> deleteDeck()
        }
    }

    private fun observeDeck() {
        if (state.value.isMutating) return
        observeJob?.cancel()
        mutableState.update { it.copy(uiState = DeckDetailUiState.Loading) }
        observeJob = viewModelScope.launch {
            try {
                combine(
                    observeDeckByIdUseCase(deckId),
                    observeFlashcardsUseCase(deckId)
                ) { deck, flashcards ->
                    deck to flashcards
                }.collect { (deck, flashcards) ->
                    if (deck != null) {
                        mutableState.update {
                            it.copy(
                                uiState = DeckDetailUiState.Content(
                                    deck,
                                    flashcards
                                )
                            )
                        }
                    } else if (!deletingDeck) {
                        mutableState.update { it.copy(uiState = DeckDetailUiState.NotFound) }
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                mutableState.update { it.copy(uiState = DeckDetailUiState.Error) }
            }
        }
    }

    private fun deleteDeck() {
        if (state.value.uiState !is DeckDetailUiState.Content ||
            state.value.isMutating || !state.value.showDeleteDeckDialog
        ) return

        deletingDeck = true
        mutableState.update {
            it.copy(showDeleteDeckDialog = false, isMutating = true, operationError = null)
        }
        viewModelScope.launch {
            try {
                deleteDeckUseCase(deckId)
                effects.trySend(DeckDetailSideEffect.NavigateToDeckList)
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                deletingDeck = false
                mutableState.update {
                    it.copy(
                        isMutating = false,
                        operationError = DeckDetailOperationError.DeleteDeck
                    )
                }
            }
        }
    }
}
