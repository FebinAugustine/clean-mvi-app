package com.febin.feature.admindashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.feature.admindashboard.domain.usecase.GetAdminDashboardUseCase
import com.febin.feature.admindashboard.presentation.intent.AdminDashboardIntent
import com.febin.feature.admindashboard.presentation.state.AdminDashboardState
import com.febin.feature.admindashboard.domain.model.AdminDashboardError
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
 * MVI ViewModel for AdminDashboard.
 * - Handles intents, calls use case, updates state.
 */
class AdminDashboardViewModel(
    private val getAdminDashboardUseCase: GetAdminDashboardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    private val _intent = Channel<AdminDashboardIntent>(Channel.BUFFERED)
    val intent = _intent.receiveAsFlow()

    init {
        handleIntents()
        loadMetrics()  // Initial load
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                when (intent) {
                    is AdminDashboardIntent.LoadMetrics, is AdminDashboardIntent.RefreshMetrics -> loadMetrics()
                    is AdminDashboardIntent.ResolveReport -> resolveReport(intent.reportId)
                    is AdminDashboardIntent.ClearError -> _state.value = _state.value.copy(error = null)
                }
            }
        }
    }

    private fun loadMetrics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getAdminDashboardUseCase().collectLatest { result ->
                when {
                    result.isLoading() -> _state.value = _state.value.copy(isLoading = true)
                    result.isSuccess() -> _state.value = _state.value.copy(
                        isLoading = false,
                        metrics = result.getOrNull(),
                        error = null
                    )
                    else -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull() as? AdminDashboardError
                    )
                }
            }
        }
    }

    private fun resolveReport(reportId: String) {
        viewModelScope.launch {
            // Call resolve use case (not implemented; demo)
            _state.value.metrics?.let { metrics ->
                val updatedReports = metrics.pendingReports.filter { it.id != reportId }
                _state.value = _state.value.copy(
                    metrics = metrics.copy(pendingReports = updatedReports)
                )
            }
            // Refresh if needed
            loadMetrics()
        }
    }

    fun sendIntent(intent: AdminDashboardIntent ) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }
}