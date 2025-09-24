package com.febin.di

import android.content.Context
import com.febin.cleanmviapp.di.appModule  // Added: Import appModule from :app
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Koin initializer.
 * - Call initKoin(context = this, extraModules = listOf(...)) in MainActivity.
 */
fun initKoin(
    androidContext: Context,
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        androidContext(androidContext)
        appDeclaration.invoke(this)
        modules(
            domainModules + dataModules + presentationModules + appModule + extraModules  // Now resolves
        )
    }
}