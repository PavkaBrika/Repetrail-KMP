package com.breckneck.core.ui.designsystem.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ColorTokensPreviewContent() {
    val colors = RepetrailTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "COLOR TOKENS",
            style = RepetrailTheme.typography.eyebrow,
            color = colors.muted,
        )

        ColorSwatchItem("background", colors.background, colors.ink)
        ColorSwatchItem("ink", colors.ink, colors.background)
        ColorSwatchItem("muted", colors.muted, Color.White)
        ColorSwatchItem("accent", colors.accent, Color.White)
        ColorSwatchItem("mint", colors.mint, colors.ink)
        ColorSwatchItem("line", colors.line, colors.ink)
        ColorSwatchItem("surface", colors.surface, colors.ink)
        ColorSwatchItem("surfaceVariant", colors.surfaceVariant, colors.ink)
        ColorSwatchItem("selectedTabBackground", colors.selectedTabBackground, colors.accent)
    }
}

@Composable
private fun ColorSwatchItem(name: String, color: Color, textColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RepetrailTheme.shapes.cardSmall,
        color = color,
        border = BorderStroke(1.dp, RepetrailTheme.colors.line),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = name,
                style = RepetrailTheme.typography.titleMedium,
                color = textColor,
            )
            Text(
                text = colorToHex(color),
                style = RepetrailTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.8f),
            )
        }
    }
}

private fun colorToHex(color: Color): String {
    val argb = (color.value shr 32).toLong()
    return "#${(argb and 0xFFFFFFFFL).toString(16).uppercase().padStart(8, '0')}"
}

@Preview
@Composable
fun ColorTokensLightPreview() {
    RepetrailTheme(darkTheme = false) {
        ColorTokensPreviewContent()
    }
}

@Preview
@Composable
fun ColorTokensDarkPreview() {
    RepetrailTheme(darkTheme = true) {
        ColorTokensPreviewContent()
    }
}

@Composable
fun TypographyTokensPreviewContent() {
    val colors = RepetrailTheme.colors
    val typography = RepetrailTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "TYPOGRAPHY TOKENS",
            style = typography.eyebrow,
            color = colors.muted,
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.card,
            color = colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TypographySample("eyebrow", "EYEBROW · 10SP BOLD", typography.eyebrow)
                TypographySample("headingLarge", "Heading Large 29sp", typography.headingLarge)
                TypographySample("headingMedium", "Heading Medium 17sp", typography.headingMedium)
                TypographySample("titleMedium", "Title Medium 14sp", typography.titleMedium)
                TypographySample("bodyMedium", "Body Medium 13sp", typography.bodyMedium)
                TypographySample("bodySmall", "Body Small 11sp", typography.bodySmall)
                TypographySample("tabLabel", "Tab Label 11sp", typography.tabLabel)
                TypographySample("badge", "Badge 11sp Bold", typography.badge)
            }
        }
    }
}

@Composable
private fun TypographySample(label: String, sampleText: String, style: TextStyle) {
    Column {
        Text(
            text = label,
            style = RepetrailTheme.typography.bodySmall,
            color = RepetrailTheme.colors.muted,
        )
        Text(
            text = sampleText,
            style = style,
            color = RepetrailTheme.colors.ink,
        )
    }
}

@Preview
@Composable
fun TypographyTokensLightPreview() {
    RepetrailTheme(darkTheme = false) {
        TypographyTokensPreviewContent()
    }
}

@Preview
@Composable
fun TypographyTokensDarkPreview() {
    RepetrailTheme(darkTheme = true) {
        TypographyTokensPreviewContent()
    }
}

@Composable
fun DimensionTokensPreviewContent() {
    val colors = RepetrailTheme.colors
    val dimensions = RepetrailTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "SPACINGS & DIMENSIONS",
            style = RepetrailTheme.typography.eyebrow,
            color = colors.muted,
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.card,
            color = colors.surface,
            shadowElevation = dimensions.elevations.card,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SpacingBar("extraSmall (4dp)", dimensions.spacings.extraSmall)
                SpacingBar("small (8dp)", dimensions.spacings.small)
                SpacingBar("medium (16dp)", dimensions.spacings.medium)
                SpacingBar("large (22dp)", dimensions.spacings.large)
                SpacingBar("extraLarge (28dp)", dimensions.spacings.extraLarge)
            }
        }

        Text(
            text = "ELEVATIONS",
            style = RepetrailTheme.typography.eyebrow,
            color = colors.muted,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ElevationCard("Card (1dp)", dimensions.elevations.card, Modifier.weight(1f))
            ElevationCard("Tonal (3dp)", dimensions.elevations.tonal, Modifier.weight(1f))
            ElevationCard("Floating (12dp)", dimensions.elevations.floating, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SpacingBar(label: String, size: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            style = RepetrailTheme.typography.bodySmall,
            color = RepetrailTheme.colors.ink,
        )
        Box(
            modifier = Modifier
                .height(12.dp)
                .width(size * 3)
                .clip(RepetrailTheme.shapes.pill)
                .background(RepetrailTheme.colors.accent),
        )
    }
}

@Composable
private fun ElevationCard(label: String, elevation: Dp, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(60.dp),
        shape = RepetrailTheme.shapes.cardSmall,
        color = RepetrailTheme.colors.surface,
        shadowElevation = elevation,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
            Text(
                text = label,
                style = RepetrailTheme.typography.bodySmall,
                color = RepetrailTheme.colors.ink,
            )
        }
    }
}

@Preview
@Composable
fun DimensionTokensLightPreview() {
    RepetrailTheme(darkTheme = false) {
        DimensionTokensPreviewContent()
    }
}

@Composable
fun ShapeTokensPreviewContent() {
    val colors = RepetrailTheme.colors
    val shapes = RepetrailTheme.shapes

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "SHAPE TOKENS",
            style = RepetrailTheme.typography.eyebrow,
            color = colors.muted,
        )

        ShapeItem("card (22dp)", shapes.card)
        ShapeItem("cardSmall (18dp)", shapes.cardSmall)
        ShapeItem("navBar (28dp)", shapes.navBar)
        ShapeItem("navBarItem (21dp)", shapes.navBarItem)
        ShapeItem("dialog (24dp)", shapes.dialog)
        ShapeItem("pill (Circle)", shapes.pill)
    }
}

@Composable
private fun ShapeItem(name: String, shape: Shape) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = shape,
        color = RepetrailTheme.colors.surface,
        border = BorderStroke(1.dp, RepetrailTheme.colors.accent),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = name,
                style = RepetrailTheme.typography.titleMedium,
                color = RepetrailTheme.colors.ink,
            )
        }
    }
}

@Preview
@Composable
fun ShapeTokensLightPreview() {
    RepetrailTheme(darkTheme = false) {
        ShapeTokensPreviewContent()
    }
}

@Preview
@Composable
fun AllDesignTokensOverviewPreview() {
    RepetrailTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ColorTokensPreviewContent()
            TypographyTokensPreviewContent()
            DimensionTokensPreviewContent()
            ShapeTokensPreviewContent()
        }
    }
}
