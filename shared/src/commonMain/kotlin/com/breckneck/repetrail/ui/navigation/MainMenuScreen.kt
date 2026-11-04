package com.breckneck.repetrail.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.repetrail.ui.components.FloatingNavigationBar

@Composable
fun MainMenuScreen() {
    RepetrailTheme {
        TabNavigator(
            tab = MainTab.Tasks,
            key = "navigator.mainTabs",
            disposeNestedNavigators = false,
            tabDisposable = { TabDisposable(it, MainTab.entries) },
        ) { tabNavigator ->
            Surface(modifier = Modifier.fillMaxSize(), color = RepetrailTheme.colors.background) {
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
                        CurrentTab()
                    }

                    FloatingNavigationBar(
                        selectedTab = tabNavigator.current as MainTab,
                        onTabSelected = { tab ->
                            if (tabNavigator.current != tab) {
                                tabNavigator.current = tab
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                    )
                }
            }
        }
    }
}
