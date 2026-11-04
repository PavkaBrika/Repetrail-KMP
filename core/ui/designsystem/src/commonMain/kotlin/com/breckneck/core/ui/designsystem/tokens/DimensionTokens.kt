package com.breckneck.core.ui.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class RepetrailSpacings(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 22.dp,
    val extraLarge: Dp = 28.dp,
)

@Immutable
data class RepetrailIconSizes(
    val small: Dp = 18.dp,
    val medium: Dp = 20.dp,
    val default: Dp = 24.dp,
    val large: Dp = 34.dp,
)

@Immutable
data class RepetrailElevations(
    val card: Dp = 1.dp,
    val tonal: Dp = 3.dp,
    val floating: Dp = 12.dp,
)

@Immutable
data class RepetrailDimensions(
    val spacings: RepetrailSpacings = RepetrailSpacings(),
    val iconSizes: RepetrailIconSizes = RepetrailIconSizes(),
    val elevations: RepetrailElevations = RepetrailElevations(),
    val navBarItemHeight: Dp = 54.dp,
)

fun defaultRepetrailDimensions(): RepetrailDimensions = RepetrailDimensions()

val LocalRepetrailDimensions = staticCompositionLocalOf { defaultRepetrailDimensions() }
