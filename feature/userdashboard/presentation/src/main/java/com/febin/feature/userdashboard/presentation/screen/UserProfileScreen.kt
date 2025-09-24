package com.febin.feature.userdashboard.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.febin.core.ui.components.CommonButton
import com.febin.core.ui.theme.AppTheme
import com.febin.feature.userdashboard.presentation.intent.UserDashboardIntent
import com.febin.feature.userdashboard.presentation.viewmodel.UserDashboardViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Display current profile
                state.dashboardData?.user?.let { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Name: ${user.name ?: "Not set"}")
                            Text("Email: ${user.email}")
                            Text("Phone: ${user.phone ?: "Not set"}")
                            Text("Role: ${user.role.name}")
                            Text("Verified: ${user.isVerified}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Edit form
                OutlinedTextField(
                    value = state.profileName,
                    onValueChange = { /* Handled in VM via intent if needed */ },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.profilePhone,
                    onValueChange = { /* Similar */ },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CommonButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.sendIntent(UserDashboardIntent.UpdateProfile(state.profileName, state.profilePhone))
                            }
                        },
                        text = "Update Profile"
                    )
                    CommonButton(
                        onClick = { navController.popBackStack() },
                        text = "Back to Dashboard"
                    )
                }

                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error.message ?: "Profile update failed",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}