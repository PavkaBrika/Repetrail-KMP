package com.breckneck.repetrail.ui.screens.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.components.PageHeading

private val sampleDecks = listOf(
    "Английский · путешествия",
    "Биология · клетка",
    "История · XX век",
    "Испанский · базовые фразы",
    "Физика · механика",
    "Химия · элементы",
)

@Composable
internal fun CardsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 22.dp,
            top = 22.dp,
            end = 22.dp,
            bottom = 112.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "heading") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PageHeading(
                    eyebrow = "ТВОЯ КАРТОТЕКА",
                    title = "Карточки",
                    subtitle = "Демонстрационный список для проверки прокрутки.",
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RepetrailTheme.shapes.cardSmall,
                    color = RepetrailTheme.colors.surface,
                    shadowElevation = RepetrailTheme.dimensions.elevations.card,
                ) {
                    Text(
                        text = "30 тестовых наборов",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        color = RepetrailTheme.colors.muted,
                        style = RepetrailTheme.typography.bodyMedium,
                    )
                }
            }
        }
        items(count = 30, key = { it }) { index ->
            val baseTitle = sampleDecks[index % sampleDecks.size]
            val deckNumber = index / sampleDecks.size + 1
            val title = if (deckNumber == 1) baseTitle else "$baseTitle $deckNumber"
            val count = 12 + (index * 7 % 55)
            DemoDeckRow(index = index + 1, title = title, count = count)
        }
        item(key = "end") {
            Text(
                text = "Конец демонстрационного списка",
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                color = RepetrailTheme.colors.muted,
                style = RepetrailTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun DemoDeckRow(index: Int, title: String, count: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RepetrailTheme.shapes.card,
        color = RepetrailTheme.colors.surface,
        shadowElevation = RepetrailTheme.dimensions.elevations.card,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.padding(end = 13.dp),
                shape = RepetrailTheme.shapes.pill,
                color = RepetrailTheme.colors.selectedTabBackground,
            ) {
                Text(
                    text = index.toString().padStart(2, '0'),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    color = RepetrailTheme.colors.accent,
                    style = RepetrailTheme.typography.badge,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = RepetrailTheme.colors.ink,
                    style = RepetrailTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$count карточек · демо-набор",
                    color = RepetrailTheme.colors.muted,
                    style = RepetrailTheme.typography.bodySmall,
                )
            }
            Text(
                text = "›",
                color = RepetrailTheme.colors.muted,
                style = RepetrailTheme.typography.headingMedium,
            )
        }
    }
}
