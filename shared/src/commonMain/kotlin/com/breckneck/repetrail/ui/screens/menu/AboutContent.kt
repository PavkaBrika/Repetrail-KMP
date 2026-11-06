package com.breckneck.repetrail.ui.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.components.PageHeading

@Composable
internal fun AboutContent(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RepetrailTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 22.dp,
                end = 22.dp,
                bottom = 112.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        TextButton(onClick = onBackClick) {
            Text(
                text = "← Назад",
                color = RepetrailTheme.colors.accent,
                style = RepetrailTheme.typography.bodyMedium,
            )
        }
        PageHeading(
            eyebrow = "REPETRAIL",
            title = "О приложении",
            subtitle = "Твоё пространство для изучения и повторения.",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.dialog,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Text(
                text = "Создавай колоды, изучай карточки и возвращайся к материалу, чтобы закрепить знания.",
                modifier = Modifier.padding(20.dp),
                color = RepetrailTheme.colors.ink,
                style = RepetrailTheme.typography.bodyMedium,
            )
        }
    }
}
