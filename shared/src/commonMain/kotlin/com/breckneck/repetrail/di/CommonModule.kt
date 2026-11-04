package com.breckneck.repetrail.di

import com.breckneck.core.common.SystemTimeProvider
import com.breckneck.core.common.TimeProvider
import org.koin.dsl.module

internal val commonModule = module {
    single<TimeProvider> { SystemTimeProvider }
}
