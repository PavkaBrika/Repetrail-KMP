package com.breckneck.repetrail.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.repetrail.ui.theme.AppAccent
import com.breckneck.repetrail.ui.theme.AppInk
import com.breckneck.repetrail.ui.theme.AppMuted

@Composable
internal fun PageHeading(eyebrow: String, title: String, subtitle: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark(tint = AppAccent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(eyebrow, color = AppMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.05.sp)
        }
        Spacer(Modifier.height(14.dp))
        Text(title, color = AppInk, fontSize = 29.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, color = AppMuted, fontSize = 13.sp, lineHeight = 19.sp)
    }
}
