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
import org.jetbrains.compose.resources.getString
import repetrail.core.ui.designsystem.generated.resources.Res
import repetrail.core.ui.designsystem.generated.resources.deck_list_error_delete
import repetrail.core.ui.designsystem.generated.resources.deck_list_error_load

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
                        ),
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
                    val errorMessage = throwable.message ?: getString(Res.string.deck_list_error_load)
                    mutableState.update {
                        it.copy(
                            uiState = DeckListUiState.Error(message = errorMessage),
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
            val errorMessage = exception.message ?: getString(Res.string.deck_list_error_delete)
            mutableState.update {
                it.copy(
                    uiState = DeckListUiState.Error(message = errorMessage),
                )
            }
        }
    }
}
