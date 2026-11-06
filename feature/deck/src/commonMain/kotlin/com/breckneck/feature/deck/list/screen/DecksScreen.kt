package com.breckneck.feature.deck.list.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.breckneck.feature.deck.list.DeckListSideEffect
import com.breckneck.feature.deck.list.DeckListViewModel

@Composable
internal fun DecksScreen(
    viewModel: DeckListViewModel,
    onNavigateToDeckForm: (Long?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
    }

    DecksContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
