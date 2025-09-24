package com.febin.di

import androidx.room.Room
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApi
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApiImpl
import com.febin.feature.admindashboard.data.repository.AdminDashboardRepositoryImpl
import com.febin.feature.authentication.data.remote.api.AuthApi
import com.febin.feature.authentication.data.remote.api.AuthApiImpl
import com.febin.feature.authentication.data.repository.AuthRepositoryImpl
import com.febin.feature.userdashboard.data.remote.api.UserDashboardApi
import com.febin.feature.userdashboard.data.remote.api.UserDashboardApiImpl
import com.febin.feature.userdashboard.data.repository.UserDashboardRepositoryImpl
import com.febin.shared.data.remote.ktor.KtorClient
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Data Koin modules: Binds repos (singles), APIs, DAOs.
 * - Global DB: AppDatabase with all entities.
 */

// Shared Data
val sharedDataModule = module {
    single<HttpClient> { KtorClient.client }
}

// Auth Data
val authDataModule = module {
    single<AuthApi> { AuthApiImpl(get()) }
    single { AuthRepositoryImpl(get(), get()) }  // Remote + Local
}

// UserDashboard Data
val userDashboardDataModule = module {
    single<UserDashboardApi> { UserDashboardApiImpl(get()) }
    single { UserDashboardRepositoryImpl(get(), get()) }
}

// AdminDashboard Data
val adminDashboardDataModule = module {
    single<AdminDashboardApi> { AdminDashboardApiImpl(get()) }
    single { AdminDashboardRepositoryImpl(get(), get()) }
}

// Global DB (Room)
val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<android.content.Context>().applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()
    }
    // DAOs (inject DB)
    single { get<AppDatabase>().authDao() }
    single { get<AppDatabase>().dashboardDao() }
    single { get<AppDatabase>().adminDao() }
}

val dataModules: List<Module> = listOf(
    sharedDataModule,
    authDataModule,
    userDashboardDataModule,
    adminDashboardDataModule,
    databaseModule
)