package com.breckneck.feature.deck.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.breckneck.feature.deck.form.DeckFormViewModel
import com.breckneck.feature.deck.form.screen.DeckFormScreen
import com.breckneck.feature.deck.list.DeckListViewModel
import com.breckneck.feature.deck.list.screen.DecksScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.deckDestinations(
    navController: NavHostController,
    onNavigateToDeckDetail: (Long) -> Unit,
) {
    composable<DeckListRoute> { entry ->
        val viewModel = koinViewModel<DeckListViewModel>(
            viewModelStoreOwner = entry,
            extras = entry.defaultViewModelCreationExtras,
        )

        DecksScreen(
            viewModel = viewModel,
            onNavigateToDeckDetail = onNavigateToDeckDetail,
            onNavigateToDeckForm = { deckId ->
                val route = when (deckId) {
                    null -> DeckFormRoute()
                    else -> DeckFormRoute(deckId)
                }
                navController.navigate(route) {
                    launchSingleTop = true
                }
            },
        )
    }
    composable<DeckFormRoute> { entry ->
        val viewModel = koinViewModel<DeckFormViewModel>(
            viewModelStoreOwner = entry,
            extras = entry.defaultViewModelCreationExtras,
        )

        DeckFormScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
        )
    }
}
