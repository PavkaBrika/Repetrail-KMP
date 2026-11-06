package com.breckneck.feature.deck.list.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme

@Composable
internal fun DeckEmptyIllustration(modifier: Modifier = Modifier) {
    val colors = RepetrailTheme.colors

    // Decorative card-stack glyph: its display size uses the large icon token.
    Canvas(modifier = modifier.size(RepetrailTheme.dimensions.iconSizes.large)) {
        // Artwork coordinates use a 48 × 48 viewport, independent of display density.
        scale(scale = size.width / 48f, pivot = Offset.Zero) {
            drawRoundRect(
                color = colors.mint,
                topLeft = Offset(4f, 4f),
                size = Size(30f, 34f),
                cornerRadius = CornerRadius(4f),
            )
            drawRoundRect(
                color = colors.line,
                topLeft = Offset(9f, 9f),
                size = Size(30f, 34f),
                cornerRadius = CornerRadius(4f),
            )
            drawRoundRect(
                color = colors.accent,
                topLeft = Offset(14f, 14f),
                size = Size(30f, 30f),
                cornerRadius = CornerRadius(4f),
            )
            drawLine(
                color = colors.onPrimary,
                start = Offset(21f, 25f),
                end = Offset(37f, 25f),
                strokeWidth = 2f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = colors.onPrimary,
                start = Offset(21f, 32f),
                end = Offset(31f, 32f),
                strokeWidth = 2f,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Preview
@Composable
private fun DeckEmptyIllustrationPreview() {
    RepetrailTheme {
        DeckEmptyIllustration()
    }
}
