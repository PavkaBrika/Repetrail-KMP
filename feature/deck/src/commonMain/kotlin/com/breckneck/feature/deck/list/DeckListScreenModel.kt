package com.breckneck.feature.deck.list

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.breckneck.core.domain.usecase.deck.DeleteDeckUseCase
import com.breckneck.core.domain.usecase.deck.ObserveDecksUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        }
    }

    private fun observeDecks() {
        observeJob?.cancel()

        observeJob = screenModelScope.launch {
            mutableState.update { it.copy(uiState = DeckListUiState.Loading) }

            observeDecksUseCase()
                .catch { throwable ->
                    mutableState.update {
                        it.copy(
                            uiState = DeckListUiState.Error(
                                message = throwable.message ?: "Не удалось загрузить список колод",
                            ),
                        )
                    }
                }
                .collect { decks ->
                    mutableState.update {
                        it.copy(
                            uiState = if (decks.isEmpty()) {
                                DeckListUiState.Empty
                            } else {
                                DeckListUiState.Content(decks = decks)
                            },
                        )
                    }
                }
        }
    }

    private fun confirmDelete() = screenModelScope.launch {
        val deck = state.value.deleteDeckDialogState.deckToDelete ?: return@launch

        mutableState.update {
            it.copy(deleteDeckDialogState = DeleteDeckDialogState.initial())
        }

        try {
            deleteDeckUseCase(deck.id)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            mutableState.update {
                it.copy(
                    uiState = DeckListUiState.Error(
                        message = exception.message ?: "Не удалось удалить колоду",
                    ),
                )
            }
        }
    }
}
