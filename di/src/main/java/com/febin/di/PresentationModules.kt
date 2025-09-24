package com.febin.di

import com.febin.feature.admindashboard.presentation.viewmodel.AdminDashboardViewModel
import com.febin.feature.authentication.presentation.viewmodel.LoginViewModel
import com.febin.feature.authentication.presentation.viewmodel.SignupViewModel
import com.febin.feature.userdashboard.presentation.viewmodel.UserDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // Auth
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<SignupViewModel> { SignupViewModel(get()) }

    // User Dashboard
    viewModel<UserDashboardViewModel> { UserDashboardViewModel(get()) }

    // Admin Dashboard
    viewModel<AdminDashboardViewModel> { AdminDashboardViewModel(get()) }
}