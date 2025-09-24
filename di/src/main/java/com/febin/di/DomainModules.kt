package com.febin.di

import com.febin.feature.admindashboard.domain.usecase.GetAdminDashboardUseCase
import com.febin.feature.authentication.domain.usecase.LoginUseCase
import com.febin.feature.authentication.domain.usecase.SignupUseCase
import com.febin.feature.dashboard.domain.usecase.GetUserDashboardUseCase
import com.febin.shared_domain.usecase.GetSharedUserUseCase
import org.koin.dsl.module

val domainModule = module {
    // Shared
    factory { GetSharedUserUseCase(get()) }

    // Auth
    factory { LoginUseCase(get()) }
    factory { SignupUseCase(get()) }

    // User Dashboard
    factory { GetUserDashboardUseCase(get()) }

    // Admin Dashboard
    factory { GetAdminDashboardUseCase(get()) }
}