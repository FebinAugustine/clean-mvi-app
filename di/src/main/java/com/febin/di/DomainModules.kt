// Updated: di/src/main/kotlin/com/febin/di/DomainModules.kt
package com.febin.di

import com.febin.feature.admindashboard.domain.usecase.GetAdminDashboardUseCase
import com.febin.feature.authentication.domain.usecase.LoginUseCase
import com.febin.feature.authentication.domain.usecase.SignupUseCase
import com.febin.feature.dashboard.domain.usecase.GetUserDashboardUseCase
import com.febin.shared_domain.usecase.GetSharedUserUseCase  // Fixed: shared_domain to match project name
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Domain Koin modules: Binds use cases (factories for fresh instances).
 */
val sharedDomainModule = module {
    factory { GetSharedUserUseCase(get()) }  // Injects UserRepository (from data)
}

val authDomainModule = module {
    factory { LoginUseCase(get()) }
    factory { SignupUseCase(get()) }
}

val userDashboardDomainModule = module {
    factory { GetUserDashboardUseCase(get()) }
}

val adminDashboardDomainModule = module {
    factory { GetAdminDashboardUseCase(get()) }
}

val domainModules: List<Module> = listOf(
    sharedDomainModule,
    authDomainModule,
    userDashboardDomainModule,
    adminDashboardDomainModule
)