package com.breckneck.feature.deck.form.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.breckneck.feature.deck.form.DeckFormIntent
import com.breckneck.feature.deck.form.DeckFormSideEffect
import com.breckneck.feature.deck.form.DeckFormViewModel

@Composable
internal fun DeckFormScreen(
    viewModel: DeckFormViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

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

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
    }

    DeckFormContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
