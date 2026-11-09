package com.breckneck.repetrail.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.feature.deck.navigation.DeckListRoute
import com.breckneck.feature.deck.navigation.deckDestinations
import com.breckneck.repetrail.ui.components.FloatingNavigationBar
import com.breckneck.repetrail.ui.screens.tasks.TasksScreen

@Preview
@Composable
fun MainMenuScreen() {
    RepetrailTheme {
        val tasksNavController = rememberNavController()
        val decksNavController = rememberNavController()
        val menuNavController = rememberNavController()
        var selectedTabName by rememberSaveable { mutableStateOf(MainTab.Tasks.name) }
        val selectedTab = MainTab.entries
            .firstOrNull { it.name == selectedTabName }
            ?: MainTab.Tasks
        val saveableStateHolder = rememberSaveableStateHolder()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = RepetrailTheme.colors.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                saveableStateHolder.SaveableStateProvider(selectedTab.name) {
                    when (selectedTab) {
                        MainTab.Tasks -> {
                            NavHost(
                                navController = tasksNavController,
                                startDestination = TasksRoute,
                                modifier = Modifier.fillMaxSize(),
                                enterTransition = forwardEnterTransition,
                                exitTransition = forwardExitTransition,
                                popEnterTransition = backEnterTransition,
                                popExitTransition = backExitTransition,
                                predictivePopEnterTransition = predictiveBackEnterTransition,
                                predictivePopExitTransition = predictiveBackExitTransition,
                            ) {
                                composable<TasksRoute> {
                                    TasksScreen()
                                }
                            }
                        }

                        MainTab.Decks -> {
                            NavHost(
                                navController = decksNavController,
                                startDestination = DeckListRoute,
                                modifier = Modifier.fillMaxSize(),
                                enterTransition = forwardEnterTransition,
                                exitTransition = forwardExitTransition,
                                popEnterTransition = backEnterTransition,
                                popExitTransition = backExitTransition,
                                predictivePopEnterTransition = predictiveBackEnterTransition,
                                predictivePopExitTransition = predictiveBackExitTransition,
                            ) {
                                deckDestinations(decksNavController)
                            }
                        }

                        MainTab.Menu -> {
                            NavHost(
                                navController = menuNavController,
                                startDestination = MenuRoute,
                                modifier = Modifier.fillMaxSize(),
                                enterTransition = forwardEnterTransition,
                                exitTransition = forwardExitTransition,
                                popEnterTransition = backEnterTransition,
                                popExitTransition = backExitTransition,
                                predictivePopEnterTransition = predictiveBackEnterTransition,
                                predictivePopExitTransition = predictiveBackExitTransition,
                            ) {
                                menuDestinations(menuNavController)
                            }
                        }
                    }
                }

                FloatingNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        if (tab != selectedTab) {
                            selectedTabName = tab.name
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(RepetrailTheme.colors.background, Color.Transparent)
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, RepetrailTheme.colors.background)
                            )
                        )
                )
            }
        }
    }
}
