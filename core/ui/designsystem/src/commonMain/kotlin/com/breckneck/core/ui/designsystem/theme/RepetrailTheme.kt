package com.breckneck.core.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.breckneck.core.ui.designsystem.tokens.LocalRepetrailColors
import com.breckneck.core.ui.designsystem.tokens.LocalRepetrailDimensions
import com.breckneck.core.ui.designsystem.tokens.LocalRepetrailShapes
import com.breckneck.core.ui.designsystem.tokens.LocalRepetrailTypography
import com.breckneck.core.ui.designsystem.tokens.RepetrailColors
import com.breckneck.core.ui.designsystem.tokens.RepetrailDimensions
import com.breckneck.core.ui.designsystem.tokens.RepetrailShapes
import com.breckneck.core.ui.designsystem.tokens.RepetrailTypography
import com.breckneck.core.ui.designsystem.tokens.darkRepetrailColors
import com.breckneck.core.ui.designsystem.tokens.defaultRepetrailDimensions
import com.breckneck.core.ui.designsystem.tokens.defaultRepetrailShapes
import com.breckneck.core.ui.designsystem.tokens.defaultRepetrailTypography
import com.breckneck.core.ui.designsystem.tokens.lightRepetrailColors

object RepetrailTheme {
    val colors: RepetrailColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRepetrailColors.current

    val typography: RepetrailTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalRepetrailTypography.current

    val dimensions: RepetrailDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalRepetrailDimensions.current

    val shapes: RepetrailShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalRepetrailShapes.current
}

@Composable
fun RepetrailTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: RepetrailColors = if (darkTheme) darkRepetrailColors() else lightRepetrailColors(),
    typography: RepetrailTypography = defaultRepetrailTypography(),
    dimensions: RepetrailDimensions = defaultRepetrailDimensions(),
    shapes: RepetrailShapes = defaultRepetrailShapes(),
    content: @Composable () -> Unit,
) {
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.accent,
            onPrimary = colors.onPrimary,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
        )
    } else {
        lightColorScheme(
            primary = colors.accent,
            onPrimary = colors.onPrimary,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
        )
    }

    CompositionLocalProvider(
        LocalRepetrailColors provides colors,
        LocalRepetrailTypography provides typography,
        LocalRepetrailDimensions provides dimensions,
        LocalRepetrailShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content,
        )
    }
}
