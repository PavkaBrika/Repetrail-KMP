package com.breckneck.feature.deck.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.breckneck.feature.deck.presentation.screen.DeckListContent

class DeckListScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DeckListScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(screenModel) {
            screenModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckListSideEffect.NavigateToDeckForm -> {
                        // Will be wired to DeckFormScreen in 2.10 / step 2.7
                    }
                    is DeckListSideEffect.NavigateToDeckDetail -> {
                        // Will be wired to DeckDetailScreen in step 3
                    }
                }
            }
        }

        DeckListContent(
            state = state,
            onIntent = screenModel::onIntent,
        )
    }
}
