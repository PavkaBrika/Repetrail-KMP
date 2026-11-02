package com.breckneck.repetrail.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

internal enum class AppIcon { Tasks, Cards, Menu }

@Composable
internal fun BrandMark(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val unit = size.minDimension / 24f
        val stroke = Stroke(width = unit * 2.4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val loop = Path().apply {
            moveTo(18.5f * unit, 8f * unit)
            lineTo(18.5f * unit, 5f * unit)
            lineTo(15.5f * unit, 5f * unit)
            moveTo(18.2f * unit, 5.3f * unit)
            cubicTo(16.7f * unit, 3.8f * unit, 14.5f * unit, 3f * unit, 12f * unit, 3f * unit)
            cubicTo(7f * unit, 3f * unit, 3f * unit, 7f * unit, 3f * unit, 12f * unit)
            cubicTo(3f * unit, 17f * unit, 7f * unit, 21f * unit, 12f * unit, 21f * unit)
            cubicTo(16.5f * unit, 21f * unit, 20.2f * unit, 17.8f * unit, 21f * unit, 13.5f * unit)
        }
        drawPath(loop, tint, style = stroke)
        drawLine(
            color = tint,
            start = Offset(8f * unit, 12f * unit),
            end = Offset(16f * unit, 12f * unit),
            strokeWidth = unit * 2.1f,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
internal fun AppIconView(icon: AppIcon, tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val unit = size.minDimension / 24f
        val strokeWidth = unit * 1.8f
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (icon) {
            AppIcon.Tasks -> {
                val path = Path().apply {
                    moveTo(4f * unit, 10f * unit)
                    lineTo(12f * unit, 4f * unit)
                    lineTo(20f * unit, 10f * unit)
                    moveTo(6f * unit, 9f * unit)
                    lineTo(6f * unit, 20f * unit)
                    lineTo(18f * unit, 20f * unit)
                    lineTo(18f * unit, 9f * unit)
                    moveTo(10f * unit, 20f * unit)
                    lineTo(10f * unit, 14f * unit)
                    lineTo(14f * unit, 14f * unit)
                    lineTo(14f * unit, 20f * unit)
                }
                drawPath(path, tint, style = stroke)
            }
            AppIcon.Cards -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(3f * unit, 5f * unit),
                    size = Size(13f * unit, 15f * unit),
                    cornerRadius = CornerRadius(2.5f * unit),
                    style = stroke,
                )
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(8f * unit, 3f * unit),
                    size = Size(13f * unit, 15f * unit),
                    cornerRadius = CornerRadius(2.5f * unit),
                    style = stroke,
                )
                drawLine(tint, Offset(11f * unit, 9f * unit), Offset(17f * unit, 9f * unit), strokeWidth, StrokeCap.Round)
                drawLine(tint, Offset(11f * unit, 13f * unit), Offset(16f * unit, 13f * unit), strokeWidth, StrokeCap.Round)
            }
            AppIcon.Menu -> {
                drawLine(tint, Offset(4f * unit, 6f * unit), Offset(20f * unit, 6f * unit), strokeWidth, StrokeCap.Round)
                drawLine(tint, Offset(4f * unit, 12f * unit), Offset(20f * unit, 12f * unit), strokeWidth, StrokeCap.Round)
                drawLine(tint, Offset(4f * unit, 18f * unit), Offset(20f * unit, 18f * unit), strokeWidth, StrokeCap.Round)
            }
        }
    }
}
