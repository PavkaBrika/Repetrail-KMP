package com.breckneck.repetrail

import androidx.compose.ui.window.ComposeUIViewController
import com.breckneck.repetrail.di.initKoinIos
import com.breckneck.repetrail.ui.navigation.MainMenuScreen
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoinIos()
    return ComposeUIViewController { MainMenuScreen() }
}