package com.breckneck.feature.deck.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.breckneck.feature.deck.form.DeckFormScreen
import com.breckneck.feature.deck.presentation.screen.DecksContent

class DecksScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<DeckListScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(screenModel) {
            screenModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckListSideEffect.NavigateToDeckForm -> {
                        navigator.push(DeckFormScreen(deckId = effect.deckId))
                    }

                    is DeckListSideEffect.NavigateToDeckDetail -> {
                        // Will be wired to DeckDetailScreen in step 3
                    }
                }
            }
        }

        DecksContent(
            state = state,
            onIntent = screenModel::onIntent,
        )
    }
}
