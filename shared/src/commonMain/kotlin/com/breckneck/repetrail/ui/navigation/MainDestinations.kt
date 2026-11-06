package com.breckneck.repetrail.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.breckneck.repetrail.ui.screens.menu.AboutContent
import com.breckneck.repetrail.ui.screens.menu.MenuScreen
import com.breckneck.repetrail.ui.screens.tasks.TasksScreen

internal fun NavGraphBuilder.mainDestinations(navController: NavHostController) {
    composable<TasksRoute> {
        TasksScreen()
    }
    composable<MenuRoute> {
        MenuScreen {
            navController.navigate(AboutRoute) {
                launchSingleTop = true
            }
        }
    }
    composable<AboutRoute> {
        AboutContent { navController.popBackStack() }
    }
}
