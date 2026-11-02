package com.breckneck.repetrail.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.repetrail.ui.components.PageHeading
import com.breckneck.repetrail.ui.theme.AppInk
import com.breckneck.repetrail.ui.theme.AppMuted

@Composable
internal fun MenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        PageHeading(
            eyebrow = "ПРОСТРАНСТВО REPETRAIL",
            title = "Меню",
            subtitle = "Настройки и профиль появятся здесь.",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 1.dp,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Пока здесь пусто", color = AppInk, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("Экран меню готов к наполнению.", color = AppMuted, fontSize = 13.sp, lineHeight = 19.sp)
            }
        }
    }
}
