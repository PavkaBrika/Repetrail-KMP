package com.breckneck.feature.deck.presentation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckListScreenModel(
    private val observeDecksUseCase: ObserveDecksUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
) : StateScreenModel<DeckListState>(DeckListState.initial()) {

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
                mutableState.update {
                    it.copy(
                        deleteDeckDialogState = DeleteDeckDialogState(
                            isVisible = true,
                            deckToDelete = intent.deck,
                        )
                    )
                }
            }

            DeckListIntent.CancelDelete -> {
                mutableState.update {
                    it.copy(deleteDeckDialogState = DeleteDeckDialogState.initial())
                }
            }

            DeckListIntent.ConfirmDelete -> confirmDelete()

            is DeckListIntent.UndoDelete -> resetAwaitDeletion()

            is DeckListIntent.SnackbarDismissed -> tryDeleteAwaitingDeck()
        }
    }

    private fun observeDecks() {
        observeJob?.cancel()

        observeJob = screenModelScope.launch {
            mutableState.update { it.copy(uiState = DeckListUiState.Loading) }

            combine(
                observeDecksUseCase(),
                state
                    .map { it.deckAwaitingDeletion?.id }
                    .distinctUntilChanged(),
            ) { allDecks, awaitingDeletionId ->
                allDecks.filterNot { it.id == awaitingDeletionId }
            }
                .catch { throwable ->
                    mutableState.update {
                        it.copy(
                            uiState = DeckListUiState.Error(
                                message = throwable.message ?: "Не удалось загрузить список колод",
                            ),
                        )
                    }
                }
                .collect { visibleDecks ->
                    mutableState.update {
                        it.copy(
                            uiState = if (visibleDecks.isEmpty()) {
                                DeckListUiState.Empty
                            } else {
                                DeckListUiState.Content(decks = visibleDecks)
                            },
                        )
                    }
                }
        }
    }

    private fun confirmDelete() = screenModelScope.launch {
        tryDeleteAwaitingDeck()

        val deck = state.value.deleteDeckDialogState.deckToDelete ?: return@launch

        mutableState.update {
            it.copy(
                deckAwaitingDeletion = state.value.deleteDeckDialogState.deckToDelete,
                deleteDeckDialogState = DeleteDeckDialogState.initial()
            )
        }

        _sideEffect.trySend(DeckListSideEffect.ShowUndoSnackbar(deck.name))
    }

    private fun tryDeleteAwaitingDeck() = screenModelScope.launch {
        val deck = state.value.deckAwaitingDeletion ?: return@launch

        deleteDeckUseCase(deck.id)
        resetAwaitDeletion()
    }

    private fun resetAwaitDeletion() {
        mutableState.update { it.copy(deckAwaitingDeletion = null) }
    }

    override fun onDispose() {
        tryDeleteAwaitingDeck()
        super.onDispose()
    }
}
