package com.breckneck.core.ui.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class RepetrailTypography(
    val eyebrow: TextStyle,
    val headingLarge: TextStyle,
    val headingMedium: TextStyle,
    val titleMedium: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val tabLabel: TextStyle,
    val badge: TextStyle,
)

fun defaultRepetrailTypography(): RepetrailTypography = RepetrailTypography(
    eyebrow = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.05.sp,
    ),
    headingLarge = TextStyle(
        fontSize = 29.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold,
    ),
    headingMedium = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyMedium = TextStyle(
        fontSize = 13.sp,
        lineHeight = 19.sp,
        fontWeight = FontWeight.Medium,
    ),
    bodySmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
    ),
    tabLabel = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
    ),
    badge = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
    ),
)

val LocalRepetrailTypography = staticCompositionLocalOf { defaultRepetrailTypography() }
