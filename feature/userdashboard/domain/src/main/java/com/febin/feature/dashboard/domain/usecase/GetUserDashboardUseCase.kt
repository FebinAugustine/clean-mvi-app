package com.febin.feature.dashboard.domain.usecase

import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.feature.dashboard.domain.model.DashboardError
import com.febin.feature.dashboard.domain.repository.UserDashboardRepository
import com.febin.shared_domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching user dashboard data.
 * - Delegates to repo; adds domain logic (e.g., offline handling).
 * - Returns Flow<Result<DashboardData>> for MVI.
 */
class GetUserDashboardUseCase(
    private val repository: UserDashboardRepository
) {
    /**
     * Invokes the use case: Get dashboard data.
     * @param userId The current user's ID.
     * @return Flow<Result<DashboardData>>.
     */
    operator fun invoke(userId: String): Flow<Result<DashboardData>> = flow {
        emit(Result.loading())

        try {
            val result = repository.getDashboardData(userId)
            when (result) {
                is Result.Success -> emit(Result.success(result.data))
                is Result.Failure -> {
                    val error = when {
                        result.exception.message?.contains("network") == true -> DashboardError.OfflineMode
                        result.exception is DashboardError.NoDataAvailable -> DashboardError.NoDataAvailable
                        else -> DashboardError.SyncFailed(result.exception.message ?: "Failed to load dashboard")
                    }
                    emit(Result.failure(error))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.failure(DashboardError.SyncFailed(e.message ?: "Unexpected error")))
        }
    }
}