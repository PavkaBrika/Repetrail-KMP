package com.breckneck.feature.deck.form.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.breckneck.feature.deck.form.DeckFormIntent
import com.breckneck.feature.deck.form.DeckFormSideEffect
import com.breckneck.feature.deck.form.DeckFormViewModel

internal class DeckFormScreen(
    private val viewModel: DeckFormViewModel,
    private val onNavigateBack: () -> Unit,
) : Screen {

    override val key: String = "screen.deck-form"

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsState()

        DeckFormBackHandler(
            onBack = {
                viewModel.onIntent(
                    if (state.showDiscardDialog) {
                        DeckFormIntent.CancelDiscard
                    } else {
                        DeckFormIntent.BackClicked
                    },
                )
            },
        )

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckFormSideEffect.DeckSaved -> {
                        // Handled by NavigateBack
                    }

                    DeckFormSideEffect.NavigateBack -> {
                        onNavigateBack()
                    }
                }
            }
        }

        DeckFormContent(
            state = state,
            onIntent = viewModel::onIntent,
        )
    }
}
