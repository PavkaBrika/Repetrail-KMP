package com.breckneck.core.ui.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class RepetrailColors(
    val background: Color,
    val ink: Color,
    val muted: Color,
    val accent: Color,
    val mint: Color,
    val line: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val selectedTabBackground: Color,
    val error: Color = Color(0xFFE53935),
    val onPrimary: Color = Color.White,
    val onBackground: Color = ink,
    val onSurface: Color = ink,
    val onSurfaceVariant: Color = muted,
)

fun lightRepetrailColors(
    background: Color = Color(0xFFF6F7FB),
    ink: Color = Color(0xFF20233A),
    muted: Color = Color(0xFF85899D),
    accent: Color = Color(0xFF635BEE),
    mint: Color = Color(0xFFB9F2D0),
    line: Color = Color(0xFFE9EAF1),
    surface: Color = Color.White,
    surfaceVariant: Color = Color(0xFFF0F1F7),
    selectedTabBackground: Color = Color(0xFFF0EFFF),
    error: Color = Color(0xFFE53935),
): RepetrailColors = RepetrailColors(
    background = background,
    ink = ink,
    muted = muted,
    accent = accent,
    mint = mint,
    line = line,
    surface = surface,
    surfaceVariant = surfaceVariant,
    selectedTabBackground = selectedTabBackground,
    error = error,
)

fun darkRepetrailColors(
    background: Color = Color(0xFF12131C),
    ink: Color = Color(0xFFF0F2FA),
    muted: Color = Color(0xFFA0A4B8),
    accent: Color = Color(0xFF7E77FF),
    mint: Color = Color(0xFF8CE3AD),
    line: Color = Color(0xFF2B2E42),
    surface: Color = Color(0xFF1E2030),
    surfaceVariant: Color = Color(0xFF26293D),
    selectedTabBackground: Color = Color(0xFF2D2A5B),
    error: Color = Color(0xFFFF6B6B),
): RepetrailColors = RepetrailColors(
    background = background,
    ink = ink,
    muted = muted,
    accent = accent,
    mint = mint,
    line = line,
    surface = surface,
    surfaceVariant = surfaceVariant,
    selectedTabBackground = selectedTabBackground,
    error = error,
    onPrimary = Color.White,
    onBackground = ink,
    onSurface = ink,
    onSurfaceVariant = muted,
)

val LocalRepetrailColors = staticCompositionLocalOf { lightRepetrailColors() }
