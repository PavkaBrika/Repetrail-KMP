package com.breckneck.feature.deck.list

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import repetrail.core.ui.designsystem.generated.resources.Res
import repetrail.core.ui.designsystem.generated.resources.deck_list_undo_snackbar_action
import repetrail.core.ui.designsystem.generated.resources.deck_list_undo_snackbar_message
import org.jetbrains.compose.resources.getString

class DeckListScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DeckListScreenModel>()
        val state by screenModel.state.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(screenModel) {
            screenModel.sideEffect.collect { effect ->
                when (effect) {
                    is DeckListSideEffect.NavigateToDeckForm -> {
                        // Will be wired to DeckFormScreen in 2.10 / step 2.7
                    }
                    is DeckListSideEffect.NavigateToDeckDetail -> {
                        // Will be wired to DeckDetailScreen in step 3
                    }
                    is DeckListSideEffect.ShowUndoSnackbar -> {
                        val message = getString(Res.string.deck_list_undo_snackbar_message, effect.deckName)
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = getString(Res.string.deck_list_undo_snackbar_action),
                            duration = SnackbarDuration.Short,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            screenModel.onIntent(intent = DeckListIntent.UndoDelete)
                        } else {
                            screenModel.onIntent(intent = DeckListIntent.SnackbarDismissed)
                        }
                    }
                }
            }
        }

        DeckListContent(
            state = state,
            onIntent = screenModel::onIntent,
            snackbarHostState = snackbarHostState,
        )
    }
}
