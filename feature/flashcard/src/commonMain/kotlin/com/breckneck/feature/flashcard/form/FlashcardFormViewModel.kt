package com.breckneck.feature.flashcard.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.breckneck.core.domain.usecase.flashcard.GetFlashcardByIdUseCase
import com.breckneck.core.domain.usecase.flashcard.SaveFlashcardUseCase
import com.breckneck.feature.flashcard.navigation.FlashcardFormRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import repetrail.feature.flashcard.generated.resources.Res
import repetrail.feature.flashcard.generated.resources.flashcard_form_required_field

class FlashcardFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFlashcardByIdUseCase: GetFlashcardByIdUseCase,
    private val saveFlashcardUseCase: SaveFlashcardUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<FlashcardFormRoute>()
    private val deckId = route.deckId
    private val flashcardId = route.flashcardId

    private val mutableState = MutableStateFlow(
        FlashcardFormState(
            deckId = deckId,
            flashcardId = flashcardId,
        ),
    )
    val state: StateFlow<FlashcardFormState> = mutableState.asStateFlow()

    private val _sideEffect = Channel<FlashcardFormSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<FlashcardFormSideEffect> = _sideEffect.receiveAsFlow()

    init {
        flashcardId?.let { loadFlashcard(it) }
    }

    fun onIntent(intent: FlashcardFormIntent) {
        when (intent) {
            is FlashcardFormIntent.TermChanged -> {
                mutableState.update {
                    it.copy(
                        term = intent.term,
                        termError = if (intent.term.isNotBlank()) null else it.termError,
                    )
                }
            }

            is FlashcardFormIntent.DefinitionChanged -> {
                mutableState.update {
                    it.copy(
                        definition = intent.definition,
                        definitionError = if (intent.definition.isNotBlank()) null else it.definitionError,
                    )
                }
            }

            is FlashcardFormIntent.ExampleChanged -> {
                mutableState.update { it.copy(example = intent.example) }
            }

            FlashcardFormIntent.SaveClicked -> save(isSaveAndAddAnother = false)
            FlashcardFormIntent.SaveAndAddAnotherClicked -> save(isSaveAndAddAnother = true)

            FlashcardFormIntent.BackClicked -> {
                if (state.value.hasUnsavedChanges) {
                    mutableState.update { it.copy(showDiscardDialog = true) }
                } else {
                    _sideEffect.trySend(FlashcardFormSideEffect.NavigateBack)
                }
            }

            FlashcardFormIntent.ConfirmDiscard -> {
                mutableState.update { it.copy(showDiscardDialog = false) }
                _sideEffect.trySend(FlashcardFormSideEffect.NavigateBack)
            }

            FlashcardFormIntent.CancelDiscard -> {
                mutableState.update { it.copy(showDiscardDialog = false) }
            }
        }
    }

    private fun loadFlashcard(id: Long) = viewModelScope.launch {
        mutableState.update { it.copy(uiState = FlashcardFormUiState.Loading) }
        val flashcard = getFlashcardByIdUseCase(deckId, id)
        if (flashcard != null) {
            mutableState.update {
                it.copy(
                    term = flashcard.term,
                    definition = flashcard.definition,
                    example = flashcard.example.orEmpty(),
                    originalTerm = flashcard.term,
                    originalDefinition = flashcard.definition,
                    originalExample = flashcard.example.orEmpty(),
                    uiState = FlashcardFormUiState.Content,
                )
            }
        } else {
            mutableState.update { it.copy(uiState = FlashcardFormUiState.Content) }
        }
    }

    private fun save(isSaveAndAddAnother: Boolean) = viewModelScope.launch {
        val trimmedTerm = state.value.term.trim()
        val trimmedDefinition = state.value.definition.trim()

        var hasError = false
        if (trimmedTerm.isBlank()) {
            val error = getString(Res.string.flashcard_form_required_field)
            mutableState.update { it.copy(termError = error) }
            hasError = true
        }
        if (trimmedDefinition.isBlank()) {
            val error = getString(Res.string.flashcard_form_required_field)
            mutableState.update { it.copy(definitionError = error) }
            hasError = true
        }
        if (hasError) return@launch

        mutableState.update { it.copy(uiState = FlashcardFormUiState.Saving) }

        val trimmedExample = state.value.example.trim().ifBlank { null }
        saveFlashcardUseCase(
            deckId = deckId,
            flashcardId = flashcardId,
            term = trimmedTerm,
            definition = trimmedDefinition,
            example = trimmedExample,
        )

        if (isSaveAndAddAnother) {
            mutableState.update {
                it.copy(
                    term = "",
                    definition = "",
                    example = "",
                    originalTerm = "",
                    originalDefinition = "",
                    originalExample = "",
                    termError = null,
                    definitionError = null,
                    uiState = FlashcardFormUiState.Content,
                )
            }
            _sideEffect.trySend(FlashcardFormSideEffect.FlashcardAddedAndContinue)
        } else {
            _sideEffect.trySend(FlashcardFormSideEffect.FlashcardSaved(isNew = flashcardId == null))
            _sideEffect.trySend(FlashcardFormSideEffect.NavigateBack)
        }
    }
}
