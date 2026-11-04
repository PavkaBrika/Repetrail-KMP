package com.breckneck.repetrail

import android.app.Application
import com.breckneck.repetrail.di.initKoinAndroid

class RepetrailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}
