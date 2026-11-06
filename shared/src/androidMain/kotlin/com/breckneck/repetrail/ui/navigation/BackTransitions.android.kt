package com.breckneck.repetrail.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.unveilIn
import androidx.compose.animation.veilOut
import androidx.navigation.NavBackStackEntry

private const val TRANSITION_DURATION_MS = 500
private val TransitionEasing = CubicBezierEasing(0.2833f, 0.99f, 0.31833f, 0.99f)

internal actual val forwardEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing),
        )
    }

@OptIn(ExperimentalAnimationApi::class)
internal actual val forwardExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            targetOffset = { it / 4 },
            animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing),
        ) + veilOut(animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing))
    }

@OptIn(ExperimentalAnimationApi::class)
internal actual val backEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            initialOffset = { it / 4 },
            animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing),
        ) + unveilIn(animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing))
    }

internal actual val backExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(TRANSITION_DURATION_MS, easing = TransitionEasing),
        )
    }

@OptIn(ExperimentalAnimationApi::class)
internal actual val predictiveBackEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> EnterTransition =
    { _ ->
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            initialOffset = { it / 4 },
            animationSpec = tween(TRANSITION_DURATION_MS, easing = LinearEasing),
        ) + unveilIn(animationSpec = tween(TRANSITION_DURATION_MS, easing = LinearEasing))
    }

internal actual val predictiveBackExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.(Int) -> ExitTransition =
    { _ ->
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(TRANSITION_DURATION_MS, easing = LinearEasing),
        )
    }
