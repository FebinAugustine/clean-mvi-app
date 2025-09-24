package com.febin.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Koin initializer.
 * - Call initKoin(context = this) in MainActivity.
 */
fun initKoin(
    androidContext: Context,
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        androidContext(androidContext)
        appDeclaration.invoke(this)
        modules(
            listOf(
                domainModule,
                dataModule,
                presentationModule,
                appModule
            )
        )
    }
}