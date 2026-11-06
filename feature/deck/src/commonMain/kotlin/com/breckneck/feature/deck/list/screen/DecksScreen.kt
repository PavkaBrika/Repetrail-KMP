package com.breckneck.feature.deck.list.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.breckneck.feature.deck.list.DeckListSideEffect
import com.breckneck.feature.deck.list.DeckListViewModel

class DecksScreen(
    private val viewModel: DeckListViewModel,
    private val onNavigateToDeckForm: (Long?) -> Unit,
) : Screen {

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckListSideEffect.NavigateToDeckForm -> {
                        onNavigateToDeckForm(effect.deckId)
                    }

                    is DeckListSideEffect.NavigateToDeckDetail -> {
                        // Will be wired to DeckDetailScreen in step 4
                    }
                }
            }
        }

        DecksContent(
            state = state,
            onIntent = viewModel::onIntent,
        )
    }
}
