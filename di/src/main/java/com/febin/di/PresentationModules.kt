package com.febin.di

import com.febin.feature.admindashboard.presentation.viewmodel.AdminDashboardViewModel
import com.febin.feature.authentication.presentation.viewmodel.LoginViewModel
import com.febin.feature.authentication.presentation.viewmodel.SignupViewModel
import com.febin.feature.userdashboard.presentation.viewmodel.UserDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Presentation Koin modules: Binds ViewModels.
 */
val authPresentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
}

val userDashboardPresentationModule = module {
    viewModel { UserDashboardViewModel(get()) }
}

val adminDashboardPresentationModule = module {
    viewModel { AdminDashboardViewModel(get()) }
}

val presentationModules: List<Module> = listOf(
    authPresentationModule,
    userDashboardPresentationModule,
    adminDashboardPresentationModule
)