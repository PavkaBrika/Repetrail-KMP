package com.breckneck.core.ui.designsystem.tokens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class RepetrailShapes(
    val card: Shape = RoundedCornerShape(22.dp),
    val cardSmall: Shape = RoundedCornerShape(18.dp),
    val navBar: Shape = RoundedCornerShape(28.dp),
    val navBarItem: Shape = RoundedCornerShape(21.dp),
    val dialog: Shape = RoundedCornerShape(24.dp),
    val pill: Shape = CircleShape,
)

fun defaultRepetrailShapes(): RepetrailShapes = RepetrailShapes()

val LocalRepetrailShapes = staticCompositionLocalOf { defaultRepetrailShapes() }
