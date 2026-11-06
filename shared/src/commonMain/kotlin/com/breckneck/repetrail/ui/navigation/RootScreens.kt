package com.breckneck.repetrail.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.breckneck.repetrail.ui.screens.menu.MenuScreen
import com.breckneck.repetrail.ui.screens.tasks.TasksScreen

internal object TasksRootScreen : Screen {
    override val key: String = "screen.tasks"

    @Composable
    override fun Content() {
        TasksScreen()
    }
}

internal object MenuRootScreen : Screen {
    override val key: String = "screen.menu"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MenuScreen(
            onAboutClick = {
                if (navigator.lastItem == this) {
                    navigator.push(AboutScreen)
                }
            }
        )
    }
}
