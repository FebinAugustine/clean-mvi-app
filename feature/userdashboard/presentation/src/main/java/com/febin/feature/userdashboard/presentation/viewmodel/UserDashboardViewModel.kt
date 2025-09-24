package com.febin.feature.userdashboard.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.feature.dashboard.domain.usecase.GetUserDashboardUseCase
import com.febin.feature.userdashboard.presentation.intent.UserDashboardIntent
import com.febin.feature.userdashboard.presentation.state.UserDashboardState
import com.febin.feature.dashboard.domain.model.DashboardError
import com.febin.shared_domain.model.exceptionOrNull
import com.febin.shared_domain.model.getOrNull
import com.febin.shared_domain.model.isLoading
import com.febin.shared_domain.model.isSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVI ViewModel for UserDashboard (handles dashboard + profile).
 * - Calls use case for data; updates state on intents.
 * - Assumes userId from auth (hardcoded for demo).
 */
class UserDashboardViewModel(
    private val getUserDashboardUseCase: GetUserDashboardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserDashboardState())
    val state: StateFlow<UserDashboardState> = _state.asStateFlow()

    private val _intent = Channel<UserDashboardIntent>(Channel.BUFFERED)
    val intent = _intent.receiveAsFlow()

    init {
        handleIntents()
        loadDashboard()  // Initial load
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                when (intent) {
                    is UserDashboardIntent.LoadDashboard, is UserDashboardIntent.RefreshDashboard -> loadDashboard()
                    is UserDashboardIntent.ViewProfile -> _state.value = _state.value.copy(isProfileEditing = !_state.value.isProfileEditing)
                    is UserDashboardIntent.UpdateProfile -> updateProfile(intent.name, intent.phone)
                    is UserDashboardIntent.ClearError -> _state.value = _state.value.copy(error = null)
                }
            }
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getUserDashboardUseCase("user123").collectLatest { result ->  // Demo userId
                when {
                    result.isLoading() -> _state.value = _state.value.copy(isLoading = true)
                    result.isSuccess() -> _state.value = _state.value.copy(
                        isLoading = false,
                        dashboardData = result.getOrNull(),
                        error = null
                    )
                    else -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull() as? DashboardError
                    )
                }
            }
        }
    }

    private fun updateProfile(name: String, phone: String) {
        viewModelScope.launch {
            // Call update use case (not implemented; demo)
            _state.value = _state.value.copy(
                profileName = name,
                profilePhone = phone,
                dashboardData = _state.value.dashboardData?.copy(user = _state.value.dashboardData!!.user.copy(name = name, phone = phone))
            )
            // Refresh dashboard if needed
            loadDashboard()
        }
    }

    fun sendIntent(intent: UserDashboardIntent ) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }
}