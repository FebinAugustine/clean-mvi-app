package com.febin.feature.userdashboard.presentation.screen

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
import com.febin.feature.dashboard.domain.model.ActivityItem
import com.febin.feature.userdashboard.presentation.intent.UserDashboardIntent
import com.febin.feature.userdashboard.presentation.viewmodel.UserDashboardViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDashboardScreen(
    navController: NavController,
    viewModel: UserDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            // Your LazyColumn content here, with padding
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                    // Welcome section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                state.dashboardData?.user?.let { user ->
                                    Text(
                                        text = "Welcome, ${user.name ?: user.email}!",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Text(
                                        text = "Role: ${user.role.name}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                CommonButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.sendIntent(UserDashboardIntent.ViewProfile)
                                        }
                                    },
                                    text = "View Profile"
                                )
                            }
                        }
                    }

                    // Metrics
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Your Metrics",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                state.dashboardData?.metrics?.let { metrics ->
                                    Text("Points: ${metrics.points} (Level ${metrics.level})")
                                    Text("Total Logins: ${metrics.totalLogins}")
                                    Text("Last Login: ${metrics.lastLogin ?: "Never"}")
                                }
                            }
                        }
                    }

                    // Recent Activity
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Recent Activity",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                state.dashboardData?.recentActivity?.let { activities ->
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(activities) { activity ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(8.dp)
                                                ) {
                                                    Text(
                                                        text = activity.title,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        text = activity.description,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                    Text(
                                                        text = activity.timestamp,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } ?: run {
                                    Text("No recent activity")
                                }
                            }
                        }
                    }

                    // Notifications badge (demo)
                    item {
                        if (state.notificationsCount > 0) {
                            BadgeContainer {
                                Text("You have ${state.notificationsCount} notifications")
                            }
                        }
                    }

                    // Refresh button
                    item {
                        CommonButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.sendIntent(UserDashboardIntent.RefreshDashboard)
                                }
                            },
                            text = "Refresh"
                        )
                    }
                }

                // Show Snackbar on error (use LaunchedEffect to trigger)
                LaunchedEffect(state.error) {
                    state.error?.let { error ->
                        snackbarHostState.showSnackbar(
                            message = error.message ?: "Error loading dashboard",
                            actionLabel = "Dismiss",
                            duration = SnackbarDuration.Short
                        )
                        viewModel.sendIntent(UserDashboardIntent.ClearError)  // Clear after showing
                    }
                }
            }
        }
    }

@Composable
private fun BadgeContainer(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box {
            content()
            Badge(
                containerColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("!")
            }
        }
    }
}