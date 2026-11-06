package com.breckneck.repetrail.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry

internal expect val backEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
internal expect val backExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
internal expect val predictiveBackEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> EnterTransition
internal expect val predictiveBackExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> ExitTransition
