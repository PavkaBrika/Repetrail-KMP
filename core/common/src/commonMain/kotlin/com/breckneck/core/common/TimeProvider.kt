package com.breckneck.core.common

import kotlin.time.Clock

/** Supplies the current Unix time in milliseconds. */
fun interface TimeProvider {
    fun currentTimeMillis(): Long
}

object SystemTimeProvider : TimeProvider {
    override fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
}
