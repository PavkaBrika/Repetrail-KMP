package com.breckneck.repetrail.ui.design.drafts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.components.PageHeading

/** Original, styled card collection retained while the active tab is a demo list. */
@Composable
internal fun CardsScreenDesignDraft() {
    Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
        PageHeading(
            eyebrow = "ТВОЯ КАРТОТЕКА",
            title = "Карточки",
            subtitle = "Собирай знания в наборы и возвращайся к ним в удобном темпе.",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.cardSmall,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("⌕", color = RepetrailTheme.colors.muted, fontSize = 24.sp)
                Spacer(Modifier.width(10.dp))
                Text("Найти набор", color = RepetrailTheme.colors.muted, style = RepetrailTheme.typography.titleMedium)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionHeading("Мои наборы", "8 наборов")
            DeckCard("Английский · A2", "Повседневные слова", "68 карточек", 0.68f, Color(0xFFEAE7FF), "Aa")
            DeckCard("Биология", "Клетка и генетика", "34 карточки", 0.42f, Color(0xFFE0F6E9), "✳")
            DeckCard("История · XX век", "Ключевые события", "26 карточек", 0.31f, Color(0xFFFFEBD7), "◷")
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.card,
            color = RepetrailTheme.colors.selectedTabBackground,
        ) {
            Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("＋", color = RepetrailTheme.colors.accent, fontSize = 25.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Создать набор", color = RepetrailTheme.colors.ink, style = RepetrailTheme.typography.titleMedium)
                    Text("Добавь карточки по своей теме", color = RepetrailTheme.colors.muted, style = RepetrailTheme.typography.bodySmall)
                }
            }
        }
    }
}
