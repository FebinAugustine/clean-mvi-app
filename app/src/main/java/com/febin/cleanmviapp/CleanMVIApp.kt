package com.febin.cleanmviapp

import android.app.Application
import com.febin.di.initKoin
import timber.log.Timber

class CleanMVIApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Init Timber for logging
        Timber.plant(Timber.DebugTree())

        // Init Koin for DI
        initKoin(this)
    }
}