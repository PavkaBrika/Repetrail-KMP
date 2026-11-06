package com.breckneck.repetrail.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.breckneck.repetrail.ui.screens.menu.AboutContent
import com.breckneck.repetrail.ui.screens.menu.MenuScreen

internal fun NavGraphBuilder.menuDestinations(navController: NavHostController) {
    composable<MenuRoute> {
        MenuScreen(
            onAboutClick = {
                navController.navigate(AboutRoute) {
                    launchSingleTop = true
                }
            },
        )
    }
    composable<AboutRoute> {
        AboutContent(onBackClick = { navController.popBackStack() })
    }
}
