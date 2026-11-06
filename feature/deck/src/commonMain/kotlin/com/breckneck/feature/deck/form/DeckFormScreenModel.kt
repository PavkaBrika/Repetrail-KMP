package com.breckneck.feature.deck.form

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.breckneck.core.domain.usecase.deck.GetDeckByIdUseCase
import com.breckneck.core.domain.usecase.deck.SaveDeckUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckFormScreenModel(
    private val deckId: Long?,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val saveDeckUseCase: SaveDeckUseCase,
) : StateScreenModel<DeckFormState>(DeckFormState(deckId = deckId)) {

    private val _sideEffect = Channel<DeckFormSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<DeckFormSideEffect> = _sideEffect.receiveAsFlow()

    init {
        deckId?.let { loadDeck(it) }
    }

    fun onIntent(intent: DeckFormIntent) {
        when (intent) {
            is DeckFormIntent.NameChanged -> {
                mutableState.update {
                    it.copy(
                        name = intent.name,
                        nameError = if (intent.name.isNotBlank()) null else it.nameError,
                    )
                }
            }

            is DeckFormIntent.DescriptionChanged -> {
                mutableState.update { it.copy(description = intent.description) }
            }

            DeckFormIntent.SaveClicked -> saveDeck()
            DeckFormIntent.BackClicked -> {
                if (state.value.hasUnsavedChanges) {
                    mutableState.update { it.copy(showDiscardDialog = true) }
                } else {
                    _sideEffect.trySend(DeckFormSideEffect.NavigateBack)
                }
            }

            DeckFormIntent.ConfirmDiscard -> {
                mutableState.update { it.copy(showDiscardDialog = false) }
                _sideEffect.trySend(DeckFormSideEffect.NavigateBack)
            }

            DeckFormIntent.CancelDiscard -> {
                mutableState.update { it.copy(showDiscardDialog = false) }
            }
        }
    }

    private fun loadDeck(id: Long) = screenModelScope.launch {
        mutableState.update { it.copy(isLoading = true) }
        try {
            val deck = getDeckByIdUseCase(id)
            if (deck != null) {
                mutableState.update {
                    it.copy(
                        name = deck.name,
                        description = deck.description.orEmpty(),
                        originalName = deck.name,
                        originalDescription = deck.description.orEmpty(),
                        isLoading = false,
                    )
                }
            } else {
                mutableState.update { it.copy(isLoading = false) }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            mutableState.update { it.copy(isLoading = false) }
        }
    }

    private fun saveDeck() = screenModelScope.launch {
        val trimmedName = state.value.name.trim()
        if (trimmedName.isBlank()) {
            mutableState.update { it.copy(nameError = "REQUIRED") }
            return@launch
        }

        mutableState.update { it.copy(isSaving = true) }

        try {
            saveDeckUseCase(
                id = deckId ?: 0L,
                name = trimmedName,
                description = state.value.description.trim().ifBlank { null },
            )

            val isNew = deckId == null
            _sideEffect.trySend(
                DeckFormSideEffect.DeckSaved(
                    deckName = trimmedName,
                    isNew = isNew,
                ),
            )
            _sideEffect.trySend(DeckFormSideEffect.NavigateBack)
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            mutableState.update { it.copy(isSaving = false) }
        }
    }
}
