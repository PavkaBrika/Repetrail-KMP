package com.breckneck.repetrail.ui.navigation

import com.breckneck.repetrail.ui.components.AppIcon

internal enum class MainTab(val title: String, val icon: AppIcon) {
    Tasks("Задания", AppIcon.Tasks),
    Cards("Карточки", AppIcon.Cards),
    Menu("Меню", AppIcon.Menu),
}
