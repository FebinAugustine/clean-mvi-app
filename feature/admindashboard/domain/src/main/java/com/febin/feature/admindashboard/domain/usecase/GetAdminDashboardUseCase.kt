package com.febin.feature.admindashboard.domain.usecase



import com.febin.feature.admindashboard.domain.model.AdminDashboardError
import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.repository.AdminDashboardRepository
import com.febin.shared_domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching admin dashboard metrics.
 * - Delegates to repo; adds domain logic (e.g., access check).
 * - Returns Flow<Result<AdminMetrics>> for MVI.
 */
class GetAdminDashboardUseCase(
    private val repository: AdminDashboardRepository
) {
    /**
     * Invokes the use case: Get admin metrics.
     * @return Flow<Result<AdminMetrics>>.
     */
    operator fun invoke(): Flow<Result<AdminMetrics>> = flow {
        emit(Result.loading())

        try {
            // Domain logic: Check admin role (assume from auth)
            val result = repository.getAdminMetrics()
            when (result) {
                is Result.Success -> emit(Result.success(result.data))
                is Result.Failure -> {
                    val error = when {
                        result.exception.message?.contains("403") == true -> AdminDashboardError.NoAdminAccess
                        result.exception is AdminDashboardError.ReportFetchFailed -> AdminDashboardError.ReportFetchFailed(result.exception.message ?: "Failed")
                        result.exception.message?.contains("network") == true -> AdminDashboardError.OfflineMode
                        else -> AdminDashboardError.ReportFetchFailed(result.exception.message ?: "Admin dashboard failed")
                    }
                    emit(Result.failure(error))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.failure(AdminDashboardError.ReportFetchFailed(e.message ?: "Unexpected error")))
        }
    }
}