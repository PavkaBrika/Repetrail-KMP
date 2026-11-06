package com.breckneck.feature.deck.form.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal actual fun DeckFormBackHandler(onBack: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isImeVisible = WindowInsets.isImeVisible

    BackHandler(
        onBack = {
            if (isImeVisible) {
                keyboardController?.hide()
            } else {
                onBack()
            }
        },
    )
}
