package com.breckneck.repetrail.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.breckneck.repetrail.ui.screens.menu.AboutContent

internal object AboutScreen : Screen {
    override val key: String = "screen.about"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AboutContent(onBackClick = { navigator.pop() })
    }
}
