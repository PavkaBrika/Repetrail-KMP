package com.breckneck.repetrail.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.DefaultNavTransitions

internal actual val forwardEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    DefaultNavTransitions.enterTransition

internal actual val forwardExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    DefaultNavTransitions.exitTransition

internal actual val backEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    DefaultNavTransitions.popEnterTransition(DefaultNavTransitions.enterTransition)

internal actual val backExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    DefaultNavTransitions.popExitTransition(DefaultNavTransitions.exitTransition)

internal actual val predictiveBackEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> EnterTransition =
    DefaultNavTransitions.predictivePopEnterTransition

internal actual val predictiveBackExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> ExitTransition =
    DefaultNavTransitions.predictivePopExitTransition
