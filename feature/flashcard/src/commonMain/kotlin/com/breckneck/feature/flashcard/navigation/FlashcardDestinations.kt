package com.breckneck.feature.flashcard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.breckneck.feature.flashcard.detail.DeckDetailViewModel
import com.breckneck.feature.flashcard.detail.screen.DeckDetailScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.flashcardDestinations(
    navController: NavHostController,
    onEditDeck: (Long) -> Unit,
    onNavigateToDeckList: () -> Unit,
) {
    composable<DeckDetailRoute> { entry ->
        val viewModel = koinViewModel<DeckDetailViewModel>(
            viewModelStoreOwner = entry,
            extras = entry.defaultViewModelCreationExtras,
        )
        DeckDetailScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToDeckList = onNavigateToDeckList,
            onEditDeck = onEditDeck,
            onOpenFlashcardForm = { deckId, flashcardId ->
                navController.navigate(FlashcardFormRoute(deckId, flashcardId)) {
                    launchSingleTop = true
                }
            },
        )
    }
}
