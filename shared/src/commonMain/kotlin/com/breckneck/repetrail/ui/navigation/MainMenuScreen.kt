package com.breckneck.repetrail.ui.navigation

import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.breckneck.repetrail.ui.components.FloatingNavigationBar
import com.breckneck.repetrail.ui.screens.cards.CardsScreen
import com.breckneck.repetrail.ui.screens.menu.MenuScreen
import com.breckneck.repetrail.ui.screens.tasks.TasksScreen
import com.breckneck.repetrail.ui.theme.AppBackground
import com.breckneck.repetrail.ui.theme.RepetrailTheme

@Composable
fun MainMenuScreen() {
    RepetrailTheme {
        var selectedTab by remember { mutableStateOf(MainTab.Tasks) }

        Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                            ),
                        ),
                ) {
                    when (selectedTab) {
                        MainTab.Tasks -> TasksScreen()
                        MainTab.Cards -> CardsScreen()
                        MainTab.Menu -> MenuScreen()
                    }
                }

                FloatingNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                )
            }
        }
    }
}
