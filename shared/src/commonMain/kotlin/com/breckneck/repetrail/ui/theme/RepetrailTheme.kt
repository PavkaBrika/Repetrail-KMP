package com.breckneck.repetrail.ui.theme

import androidx.compose.runtime.Composable
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme as DesignSystemRepetrailTheme

@Composable
fun RepetrailTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    DesignSystemRepetrailTheme(
        darkTheme = darkTheme,
        content = content,
    )
}
