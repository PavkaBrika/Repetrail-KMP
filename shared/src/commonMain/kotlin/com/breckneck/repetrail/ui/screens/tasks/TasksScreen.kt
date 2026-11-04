package com.breckneck.repetrail.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
internal fun TasksScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        PageHeading(
            eyebrow = "ТВОЁ ПРОСТРАНСТВО",
            title = "Задания",
            subtitle = "Здесь появятся твои учебные задания.",
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
                    text = "Экран заданий готов к наполнению.",
                    color = RepetrailTheme.colors.muted,
                    style = RepetrailTheme.typography.bodyMedium,
                )
            }
        }
    }
}
