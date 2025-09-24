package com.febin.feature.admindashboard.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.febin.core.ui.components.CommonButton
import com.febin.core.ui.theme.AppTheme
import com.febin.feature.admindashboard.domain.model.ReportItem
import com.febin.feature.admindashboard.presentation.intent.AdminDashboardIntent
import com.febin.feature.admindashboard.presentation.viewmodel.AdminDashboardViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminDashboardScreen(
    navController: NavController,
    viewModel: AdminDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            if (state.isLoading && state.metrics == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Metrics section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Admin Metrics",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                state.metrics?.let { metrics ->
                                    Text("Total Users: ${metrics.totalUsers}")
                                    Text("Active Users: ${metrics.activeUsers}")
                                    Text("Pending Reports: ${metrics.pendingReports.size}")
                                    Text("System Uptime: ${metrics.systemUptime ?: "N/A"}")
                                } ?: Text("No metrics available")
                            }
                        }
                    }

                    // Pending Reports
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Pending Reports",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                state.metrics?.pendingReports?.let { reports ->
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(reports) { report ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(12.dp)
                                                ) {
                                                    Text(text = report.title, style = MaterialTheme.typography.bodyMedium)
                                                    Text(text = report.description, style = MaterialTheme.typography.bodySmall)
                                                    Text(text = "Status: ${report.status.name}", style = MaterialTheme.typography.bodySmall)
                                                    Text(text = report.timestamp, style = MaterialTheme.typography.bodySmall)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    CommonButton(
                                                        onClick = {
                                                            coroutineScope.launch {
                                                                viewModel.sendIntent(AdminDashboardIntent.ResolveReport(report.id))
                                                            }
                                                        },
                                                        text = "Resolve"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } ?: run {
                                    Text("No pending reports")
                                }
                            }
                        }
                    }

                    // Recent Admins (if any)
                    item {
                        if (state.metrics?.recentAdmins?.isNotEmpty() == true) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Recent Admins",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    state.metrics!!.recentAdmins.forEach { admin ->
                                        Text(admin.name ?: admin.email)
                                    }
                                }
                            }
                        }
                    }

                    // Refresh button
                    item {
                        CommonButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.sendIntent(AdminDashboardIntent.RefreshMetrics)
                                }
                            },
                            text = "Refresh"
                        )
                    }
                }
            }
        }

        // Show Snackbar on error (use LaunchedEffect to trigger)
        LaunchedEffect(state.error) {
            state.error?.let { error ->
                snackbarHostState.showSnackbar(
                    message = error.message ?: "Error loading admin dashboard",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
                viewModel.sendIntent(AdminDashboardIntent.ClearError)  // Clear after showing
            }
        }
    }
}