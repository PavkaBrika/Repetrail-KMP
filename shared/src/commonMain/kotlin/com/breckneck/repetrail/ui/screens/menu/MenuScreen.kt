package com.breckneck.repetrail.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.components.PageHeading

@Composable
internal fun MenuScreen(onAboutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 22.dp,
                end = 22.dp,
                bottom = 112.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        PageHeading(
            eyebrow = "ПРОСТРАНСТВО REPETRAIL",
            title = "Меню",
            subtitle = "Настройки и профиль появятся здесь.",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.dialog,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Пока здесь пусто",
                    color = RepetrailTheme.colors.ink,
                    style = RepetrailTheme.typography.headingMedium,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Экран меню готов к наполнению.",
                    color = RepetrailTheme.colors.muted,
                    style = RepetrailTheme.typography.bodyMedium,
                )
            }
        }
        Surface(
            onClick = onAboutClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.card,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Text(
                text = "О приложении",
                modifier = Modifier.padding(20.dp),
                color = RepetrailTheme.colors.accent,
                style = RepetrailTheme.typography.titleMedium,
            )
        }
    }
}
