package com.breckneck.repetrail.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val AppBackground = Color(0xFFF6F7FB)
internal val AppInk = Color(0xFF20233A)
internal val AppMuted = Color(0xFF85899D)
internal val AppAccent = Color(0xFF635BEE)
internal val AppMint = Color(0xFFB9F2D0)
internal val AppLine = Color(0xFFE9EAF1)

private val RepetrailColorScheme = lightColorScheme(
    primary = AppAccent,
    onPrimary = Color.White,
    background = AppBackground,
    onBackground = AppInk,
    surface = Color.White,
    onSurface = AppInk,
    surfaceVariant = Color(0xFFF0F1F7),
    onSurfaceVariant = AppMuted,
)

@Composable
fun RepetrailTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = RepetrailColorScheme, content = content)
}
