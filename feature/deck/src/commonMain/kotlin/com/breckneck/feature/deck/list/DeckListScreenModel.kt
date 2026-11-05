package com.breckneck.feature.deck.list

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DeckListScreenModel(
    private val observeDecksUseCase: ObserveDecksUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
) : StateScreenModel<DeckListState>(DeckListState()) {

    private val _sideEffect = Channel<DeckListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<DeckListSideEffect> = _sideEffect.receiveAsFlow()

    private var observeJob: Job? = null

    init {
        observeDecks()
    }

    fun onIntent(intent: DeckListIntent) {
        when (intent) {
            DeckListIntent.Retry -> observeDecks()
            is DeckListIntent.DeckClicked -> {
                _sideEffect.trySend(DeckListSideEffect.NavigateToDeckDetail(intent.deckId))
            }
            DeckListIntent.CreateDeckClicked -> {
                _sideEffect.trySend(DeckListSideEffect.NavigateToDeckForm(null))
            }
            is DeckListIntent.EditDeckClicked -> {
                _sideEffect.trySend(DeckListSideEffect.NavigateToDeckForm(intent.deckId))
            }
            is DeckListIntent.DeleteDeckClicked -> {
                mutableState.value = state.value.copy(deckToDeleteConfirmation = intent.deck)
            }
            DeckListIntent.CancelDelete -> {
                mutableState.value = state.value.copy(deckToDeleteConfirmation = null)
            }
            DeckListIntent.ConfirmDelete -> {
                val deckToDelete = state.value.deckToDeleteConfirmation ?: return
                val currentPending = state.value.pendingDeleteDeck

                if (currentPending != null) {
                    executePhysicalDelete(currentPending)
                }

                val currentUiState = state.value.uiState
                val updatedUiState = if (currentUiState is DeckListUiState.Content) {
                    val remaining = currentUiState.decks.filter { it.id != deckToDelete.id }
                    if (remaining.isEmpty()) DeckListUiState.Empty else DeckListUiState.Content(remaining)
                } else {
                    currentUiState
                }

                mutableState.value = state.value.copy(
                    uiState = updatedUiState,
                    deckToDeleteConfirmation = null,
                    pendingDeleteDeck = deckToDelete,
                )
                _sideEffect.trySend(DeckListSideEffect.ShowUndoSnackbar(deckToDelete.name))
            }
            DeckListIntent.UndoDelete -> {
                mutableState.value = state.value.copy(pendingDeleteDeck = null)
                observeDecks()
            }
            DeckListIntent.SnackbarDismissed -> {
                val pending = state.value.pendingDeleteDeck ?: return
                executePhysicalDelete(pending)
                mutableState.value = state.value.copy(pendingDeleteDeck = null)
            }
        }
    }

    private fun observeDecks() {
        observeJob?.cancel()

        observeJob = screenModelScope.launch {
            mutableState.value = state.value.copy(uiState = DeckListUiState.Loading)

            observeDecksUseCase()
                .catch { throwable ->
                    mutableState.value = state.value.copy(
                        uiState = DeckListUiState.Error(
                            message = throwable.message ?: "Не удалось загрузить список колод",
                        )
                    )
                }
                .collect { allDecks ->
                    val pendingId = state.value.pendingDeleteDeck?.id
                    val filteredDecks = if (pendingId != null) {
                        allDecks.filter { it.id != pendingId }
                    } else {
                        allDecks
                    }

                    val newUiState = if (filteredDecks.isEmpty()) {
                        DeckListUiState.Empty
                    } else {
                        DeckListUiState.Content(decks = filteredDecks)
                    }

                    mutableState.value = state.value.copy(uiState = newUiState)
                }
        }
    }

    private fun executePhysicalDelete(deck: Deck) {
        screenModelScope.launch {
            try {
                deleteDeckUseCase(deck.id)
            } catch (_: Throwable) {
                // Ignore delete errors or handle as needed
            }
        }
    }

    override fun onDispose() {
        val pending = state.value.pendingDeleteDeck
        if (pending != null) {
            executePhysicalDelete(pending)
        }
        super.onDispose()
    }
}
