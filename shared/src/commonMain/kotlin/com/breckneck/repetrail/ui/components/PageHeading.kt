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
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme

@Composable
internal fun PageHeading(eyebrow: String, title: String, subtitle: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark(
                tint = RepetrailTheme.colors.accent,
                modifier = Modifier.size(RepetrailTheme.dimensions.iconSizes.small),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = eyebrow,
                color = RepetrailTheme.colors.muted,
                style = RepetrailTheme.typography.eyebrow,
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = title,
            color = RepetrailTheme.colors.ink,
            style = RepetrailTheme.typography.headingLarge,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = RepetrailTheme.colors.muted,
            style = RepetrailTheme.typography.bodyMedium,
        )
    }
}
