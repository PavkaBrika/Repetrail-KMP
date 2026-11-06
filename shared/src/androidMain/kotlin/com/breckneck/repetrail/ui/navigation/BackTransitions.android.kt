package com.breckneck.repetrail.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

private const val BACK_TRANSITION_DURATION_MS = 350

internal actual val backEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    { fadeIn(animationSpec = tween(BACK_TRANSITION_DURATION_MS)) }

internal actual val backExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    { slideOutHorizontally(animationSpec = tween(BACK_TRANSITION_DURATION_MS, easing = LinearEasing), targetOffsetX = { it }) }

internal actual val predictiveBackEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> EnterTransition =
    { _ -> fadeIn(animationSpec = tween(BACK_TRANSITION_DURATION_MS)) }

internal actual val predictiveBackExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> ExitTransition =
    { _ -> slideOutHorizontally(animationSpec = tween(BACK_TRANSITION_DURATION_MS, easing = LinearEasing), targetOffsetX = { it }) }
