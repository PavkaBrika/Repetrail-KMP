package com.breckneck.feature.flashcard.form

import androidx.lifecycle.SavedStateHandle
import com.breckneck.core.common.TimeProvider
import com.breckneck.core.domain.model.Flashcard
import com.breckneck.core.domain.repository.FlashcardRepository
import com.breckneck.core.domain.usecase.flashcard.GetFlashcardByIdUseCase
import com.breckneck.core.domain.usecase.flashcard.SaveFlashcardUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FlashcardFormViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeFlashcardRepository : FlashcardRepository {
        val cards = mutableMapOf<Long, Flashcard>()
        override suspend fun getFlashcardById(deckId: Long, flashcardId: Long): Flashcard? = cards[flashcardId]
        override fun observeFlashcards(deckId: Long): Flow<List<Flashcard>> =
            flowOf(cards.values.filter { it.deckId == deckId })
        override suspend fun createFlashcard(flashcard: Flashcard): Long {
            val id = (cards.keys.maxOrNull() ?: 0L) + 1L
            cards[id] = flashcard.copy(id = id)
            return id
        }
        override suspend fun updateFlashcard(flashcard: Flashcard): Boolean {
            cards[flashcard.id] = flashcard
            return true
        }
        override suspend fun deleteFlashcard(deckId: Long, flashcardId: Long): Boolean {
            return cards.remove(flashcardId) != null
        }
    }

    private class FakeTimeProvider : TimeProvider {
        override fun currentTimeMillis(): Long = 1000L
    }

    private lateinit var flashcardRepository: FakeFlashcardRepository
    private lateinit var getFlashcardByIdUseCase: GetFlashcardByIdUseCase
    private lateinit var saveFlashcardUseCase: SaveFlashcardUseCase

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        flashcardRepository = FakeFlashcardRepository()
        getFlashcardByIdUseCase = GetFlashcardByIdUseCase(flashcardRepository)
        saveFlashcardUseCase = SaveFlashcardUseCase(flashcardRepository, FakeTimeProvider())

        flashcardRepository.cards[10L] = Flashcard(
            id = 10L,
            deckId = 1L,
            term = "Apple",
            definition = "A fruit",
            example = "I eat an apple",
            createdAt = 100L,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(deckId: Long, flashcardId: Long? = null): FlashcardFormViewModel {
        val map = mutableMapOf<String, Any?>("deckId" to deckId)
        if (flashcardId != null) {
            map["flashcardId"] = flashcardId
        }
        val savedStateHandle = SavedStateHandle(map)
        return FlashcardFormViewModel(
            savedStateHandle = savedStateHandle,
            getFlashcardByIdUseCase = getFlashcardByIdUseCase,
            saveFlashcardUseCase = saveFlashcardUseCase,
        )
    }

    @Test
    fun loadData_createMode_setsContentWithEmptyFields() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(FlashcardFormUiState.Content, state.uiState)
        assertFalse(state.isEditMode)
        assertEquals("", state.term)
        assertEquals("", state.definition)
        assertEquals("", state.example)
        assertFalse(state.hasUnsavedChanges)
    }

    @Test
    fun loadData_editMode_cardExists_setsContentWithCardData() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L, flashcardId = 10L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(FlashcardFormUiState.Content, state.uiState)
        assertTrue(state.isEditMode)
        assertEquals("Apple", state.term)
        assertEquals("A fruit", state.definition)
        assertEquals("I eat an apple", state.example)
        assertFalse(state.hasUnsavedChanges)
    }

    @Test
    fun textChanges_updateFieldsAndUnsavedChanges() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L)
        advanceUntilIdle()

        viewModel.onIntent(FlashcardFormIntent.TermChanged("Banana"))
        assertEquals("Banana", viewModel.state.value.term)
        assertTrue(viewModel.state.value.hasUnsavedChanges)

        viewModel.onIntent(FlashcardFormIntent.DefinitionChanged("Yellow fruit"))
        assertEquals("Yellow fruit", viewModel.state.value.definition)

        viewModel.onIntent(FlashcardFormIntent.ExampleChanged("Monkeys love bananas"))
        assertEquals("Monkeys love bananas", viewModel.state.value.example)
        assertTrue(viewModel.state.value.hasUnsavedChanges)
    }

    @Test
    fun backClicked_withUnsavedChanges_showsDiscardDialog() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L)
        advanceUntilIdle()

        viewModel.onIntent(FlashcardFormIntent.TermChanged("Test"))
        viewModel.onIntent(FlashcardFormIntent.BackClicked)

        assertTrue(viewModel.state.value.showDiscardDialog)

        viewModel.onIntent(FlashcardFormIntent.CancelDiscard)
        assertFalse(viewModel.state.value.showDiscardDialog)
    }

    @Test
    fun saveClicked_savesCard() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L)
        advanceUntilIdle()

        viewModel.onIntent(FlashcardFormIntent.TermChanged("Pear"))
        viewModel.onIntent(FlashcardFormIntent.DefinitionChanged("A sweet fruit"))
        viewModel.onIntent(FlashcardFormIntent.SaveClicked)
        advanceUntilIdle()

        assertEquals(2, flashcardRepository.cards.size)
    }

    @Test
    fun saveAndAddAnother_clearsFields() = runTest(testDispatcher) {
        val viewModel = createViewModel(deckId = 1L)
        advanceUntilIdle()

        viewModel.onIntent(FlashcardFormIntent.TermChanged("Peach"))
        viewModel.onIntent(FlashcardFormIntent.DefinitionChanged("Juicy fruit"))
        viewModel.onIntent(FlashcardFormIntent.SaveAndAddAnotherClicked)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(FlashcardFormUiState.Content, state.uiState)
        assertEquals("", state.term)
        assertEquals("", state.definition)
        assertEquals("", state.example)
        assertFalse(state.hasUnsavedChanges)
        assertEquals(2, flashcardRepository.cards.size)
    }
}
