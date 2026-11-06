package com.breckneck.feature.deck.form.screen

import androidx.compose.runtime.Composable

@Composable
internal actual fun DeckFormBackHandler(onBack: () -> Unit) {
    // Voyager has no iOS system back handler. Interactive swipe back is planned for step 3.
}
