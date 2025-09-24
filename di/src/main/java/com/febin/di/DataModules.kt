package com.febin.di

import androidx.room.Room
import com.febin.core.data.local.AppDatabase
import com.febin.core.data.network.KtorClient
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApi
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApiImpl
import com.febin.feature.admindashboard.data.repository.AdminDashboardRepositoryImpl
import com.febin.feature.authentication.data.local.datasource.LocalAuthDataSource
import com.febin.feature.authentication.data.remote.api.AuthApi
import com.febin.feature.authentication.data.remote.api.AuthApiImpl
import com.febin.feature.authentication.data.remote.datasource.RemoteAuthDataSource
import com.febin.feature.authentication.data.repository.AuthRepositoryImpl
import com.febin.feature.userdashboard.data.repository.UserDashboardRepositoryImpl
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    // Network
    single<HttpClient> { KtorClient.client }

    // APIs
    single<AuthApi> { AuthApiImpl(get()) }
    single { com.febin.feature.userdashboard.data.remote.api.UserDashboardApiImpl(get()) }
    single<AdminDashboardApi> { AdminDashboardApiImpl(get()) }

    // DataSources
    single { RemoteAuthDataSource(get()) }
    single { LocalAuthDataSource(get()) }

    // Repositories
    single { AuthRepositoryImpl(get(), get()) }
    single { UserDashboardRepositoryImpl(get(), get()) }
    single { AdminDashboardRepositoryImpl(get(), get()) }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "app_database"
        ).build()
    }

    // DAOs
    single { get<AppDatabase>().authDao() }
    single { get<AppDatabase>().dashboardDao() }
    single { get<AppDatabase>().adminDao() }
}