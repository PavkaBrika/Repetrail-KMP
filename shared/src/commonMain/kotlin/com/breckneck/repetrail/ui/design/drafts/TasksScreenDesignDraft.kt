package com.breckneck.repetrail.ui.design.drafts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.breckneck.repetrail.ui.components.AppIcon
import com.breckneck.repetrail.ui.components.PageHeading

/** Original, styled dashboard retained while the active tab is a placeholder. */
@Composable
internal fun TasksScreenDesignDraft(onStartReview: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
        PageHeading(
            eyebrow = "ТВОЁ ПРОСТРАНСТВО",
            title = "С возвращением!",
            subtitle = "Немного практики сегодня — больше уверенности завтра.",
        )
        TodayCard(onStartReview)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SmallStatCard(
                modifier = Modifier.weight(1f),
                label = "ТВОЯ СЕРИЯ",
                value = "4 дня",
                note = "Продолжай в том же ритме",
                accent = Color(0xFFFFE8B8),
                glyph = "✦",
            )
            SmallStatCard(
                modifier = Modifier.weight(1f),
                label = "В КОЛЛЕКЦИИ",
                value = "128",
                note = "карточек для повторения",
                accent = Color(0xFFE2DEFF),
                glyph = "▤",
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionHeading("План на сегодня", "2 шага")
            TaskRow(AppIcon.Decks, "Повторить карточки", "12 карточек · около 5 минут", "НАЧАТЬ")
            TaskRow(AppIcon.Tasks, "Закрепить сложное", "Вернись к словам, которые запомнить труднее", "5 ШТУК")
        }
    }
}
