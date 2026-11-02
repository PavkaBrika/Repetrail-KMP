package com.breckneck.repetrail.ui.screens.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.repetrail.ui.components.PageHeading
import com.breckneck.repetrail.ui.theme.AppAccent
import com.breckneck.repetrail.ui.theme.AppInk
import com.breckneck.repetrail.ui.theme.AppMuted

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
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                ) {
                    Text(
                        text = "30 тестовых наборов",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        color = AppMuted,
                        fontSize = 13.sp,
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
                color = AppMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun DemoDeckRow(index: Int, title: String, count: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.padding(end = 13.dp),
                shape = CircleShape,
                color = Color(0xFFF0EFFF),
            ) {
                Text(
                    text = index.toString().padStart(2, '0'),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    color = AppAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = AppInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text("$count карточек · демо-набор", color = AppMuted, fontSize = 11.sp)
            }
            Text("›", color = AppMuted, fontSize = 22.sp)
        }
    }
}
