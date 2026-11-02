package com.breckneck.repetrail.ui.design.drafts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.repetrail.ui.components.BrandMark
import com.breckneck.repetrail.ui.components.PageHeading
import com.breckneck.repetrail.ui.theme.AppInk
import com.breckneck.repetrail.ui.theme.AppMint

/** Original, styled menu retained while the active tab is a placeholder. */
@Composable
internal fun MenuScreenDesignDraft() {
    Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
        PageHeading(
            eyebrow = "ПРОСТРАНСТВО REPETRAIL",
            title = "Меню",
            subtitle = "Настрой приложение под свой ритм обучения.",
        )
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), color = AppInk) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    BrandMark(tint = AppMint, modifier = Modifier.size(24.dp))
                }
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    Text("Твой профиль", color = Color.White, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Персональные настройки", color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
                }
                Text("→", color = AppMint, fontSize = 20.sp)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SectionHeading("Настройки", null)
            MenuOption("Цель обучения", "Задай темп на каждый день", "◎")
            MenuOption("Напоминания", "Выбери удобное время", "◷")
            MenuOption("Внешний вид", "Тема и оформление", "◐")
            MenuOption("О приложении", "Repetrail · версия 1.0", "i")
        }
    }
}
