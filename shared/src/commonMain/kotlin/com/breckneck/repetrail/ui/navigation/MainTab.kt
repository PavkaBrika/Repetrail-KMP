package com.breckneck.repetrail.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.breckneck.repetrail.ui.components.AppIcon

internal enum class MainTab(
    val title: String,
    val icon: AppIcon,
    private val rootScreen: Screen,
) : Tab {
    Tasks("Задания", AppIcon.Tasks, TasksRootScreen),
    Decks("Карточки", AppIcon.Decks, DecksRootScreen),
    Menu("Меню", AppIcon.Menu, MenuRootScreen);

    override val key: String get() = "tab.$name"

    override val options: TabOptions
        @Composable
        get() = remember(this) {
            TabOptions(
                index = ordinal.toUShort(),
                title = title
            )
        }

    @Composable
    override fun Content() {
        Navigator(
            screen = rootScreen,
            key = "navigator.$name",
        )
    }
}
