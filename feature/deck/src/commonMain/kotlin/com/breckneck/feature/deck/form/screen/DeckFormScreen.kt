package com.breckneck.feature.deck.form.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.breckneck.feature.deck.form.DeckFormIntent
import com.breckneck.feature.deck.form.DeckFormScreenModel
import com.breckneck.feature.deck.form.DeckFormSideEffect
import org.koin.core.parameter.parametersOf

internal data class DeckFormScreen(
    val deckId: Long? = null,
    private val onNavigateBack: (() -> Unit)? = null,
) : Screen {

    override val key: String get() = "screen.deck-form-${deckId ?: "new"}"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<DeckFormScreenModel> { parametersOf(deckId) }
        val state by screenModel.state.collectAsState()

        DeckFormBackHandler(
            onBack = {
                screenModel.onIntent(
                    if (state.showDiscardDialog) {
                        DeckFormIntent.CancelDiscard
                    } else {
                        DeckFormIntent.BackClicked
                    },
                )
            },
        )

        LaunchedEffect(screenModel) {
            screenModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckFormSideEffect.DeckSaved -> {
                        // Handled by NavigateBack
                    }

                    DeckFormSideEffect.NavigateBack -> {
                        if (onNavigateBack != null) {
                            onNavigateBack.invoke()
                        } else {
                            navigator.pop()
                        }
                    }
                }
            }
        }

        DeckFormContent(
            state = state,
            onIntent = screenModel::onIntent,
        )
    }
}
