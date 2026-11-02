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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breckneck.repetrail.ui.components.AppIcon
import com.breckneck.repetrail.ui.components.AppIconView
import com.breckneck.repetrail.ui.theme.AppAccent
import com.breckneck.repetrail.ui.theme.AppInk
import com.breckneck.repetrail.ui.theme.AppMint
import com.breckneck.repetrail.ui.theme.AppMuted

/** Components retained for the original tab designs in ui.design.drafts. */
@Composable
internal fun TodayCard(onStartReview: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(30.dp), color = AppInk) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
                    Text(
                        text = "СЕГОДНЯ",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                    )
                }
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("12", color = AppMint, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("Время повторить", color = Color.White, fontSize = 25.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "12 карточек ждут тебя. Это займёт всего несколько минут.",
                color = Color.White.copy(alpha = 0.68f),
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onStartReview,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = AppMint, contentColor = AppInk),
                ) {
                    Text("Начать повторение", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text("→", fontSize = 18.sp)
                }
                Spacer(Modifier.weight(1f))
                Text("≈ 5 мин", color = Color.White.copy(alpha = 0.68f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
internal fun SmallStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    note: String,
    accent: Color,
    glyph: String,
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 2.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape).background(accent),
                contentAlignment = Alignment.Center,
            ) {
                Text(glyph, color = AppInk, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(14.dp))
            Text(label, color = AppMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, color = AppInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(note, color = AppMuted, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

@Composable
internal fun TaskRow(glyph: AppIcon, title: String, subtitle: String, tag: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = Color.White, shadowElevation = 1.dp) {
        Row(modifier = Modifier.padding(horizontal = 15.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(15.dp)).background(Color(0xFFF0F0FF)),
                contentAlignment = Alignment.Center,
            ) {
                AppIconView(glyph, AppAccent, Modifier.size(21.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp, end = 8.dp)) {
                Text(title, color = AppInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = AppMuted, fontSize = 11.sp, lineHeight = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Text(tag, color = AppAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
internal fun DeckCard(title: String, subtitle: String, count: String, progress: Float, tint: Color, glyph: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(17.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(tint),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(glyph, color = AppInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(title, color = AppInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(3.dp))
                    Text(subtitle, color = AppMuted, fontSize = 12.sp)
                }
                Text("···", color = AppMuted, fontSize = 20.sp)
            }
            Spacer(Modifier.height(17.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(count, color = AppMuted, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                Text("${(progress * 100).toInt()}%", color = AppAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(Color(0xFFF0F0F5))) {
                Box(
                    modifier = Modifier.fillMaxWidth(progress).height(6.dp).clip(CircleShape).background(AppAccent),
                )
            }
        }
    }
}

@Composable
internal fun MenuOption(title: String, subtitle: String, glyph: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 1.dp) {
        Row(modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF0F0FF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(glyph, color = AppAccent, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(title, color = AppInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Text(subtitle, color = AppMuted, fontSize = 11.sp)
            }
            Text("›", color = AppMuted, fontSize = 22.sp)
        }
    }
}

@Composable
internal fun SectionHeading(title: String, trailing: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = AppInk, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        if (trailing != null) {
            Text(trailing, color = AppMuted, fontSize = 11.sp)
        }
    }
}
