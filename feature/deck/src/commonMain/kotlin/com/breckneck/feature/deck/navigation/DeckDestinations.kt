package com.breckneck.feature.deck.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cafe.adriel.voyager.navigator.Navigator
import com.breckneck.feature.deck.form.screen.DeckFormScreen
import com.breckneck.feature.deck.list.screen.DecksScreen

fun NavGraphBuilder.deckDestinations(navController: NavHostController) {
    composable<DeckListRoute> {
        Navigator(
            DecksScreen { deckId ->
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
    composable<DeckFormRoute> { backStackEntry ->
        val deckId = backStackEntry.toRoute<DeckFormRoute>().deckId
        Navigator(
            DeckFormScreen(deckId = deckId) {
                navController.popBackStack()
            },
        )
    }
}
