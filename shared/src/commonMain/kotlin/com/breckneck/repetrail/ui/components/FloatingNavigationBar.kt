package com.breckneck.repetrail.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.navigation.MainTab

@Composable
internal fun FloatingNavigationBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RepetrailTheme.shapes.navBar,
        color = RepetrailTheme.colors.surface,
        shadowElevation = RepetrailTheme.dimensions.elevations.floating,
        tonalElevation = RepetrailTheme.dimensions.elevations.tonal,
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, RepetrailTheme.colors.line.copy(alpha = 0.8f), RepetrailTheme.shapes.navBar)
                .padding(7.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            MainTab.entries.forEach { tab ->
                val selected = tab == selectedTab
                val foreground = if (selected) RepetrailTheme.colors.accent else RepetrailTheme.colors.muted
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(RepetrailTheme.dimensions.navBarItemHeight)
                        .clip(RepetrailTheme.shapes.navBarItem)
                        .background(if (selected) RepetrailTheme.colors.selectedTabBackground else Color.Transparent)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 7.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppIconView(
                        icon = tab.icon,
                        tint = foreground,
                        modifier = Modifier.size(RepetrailTheme.dimensions.iconSizes.medium),
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = tab.title,
                        color = foreground,
                        style = RepetrailTheme.typography.tabLabel.copy(
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
