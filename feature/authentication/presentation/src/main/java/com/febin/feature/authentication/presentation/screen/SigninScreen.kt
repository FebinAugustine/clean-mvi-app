package com.febin.feature.authentication.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.febin.core.ui.components.CommonButton
import com.febin.core.ui.theme.AppTheme
import com.febin.feature.authentication.presentation.intent.LoginIntent
import com.febin.feature.authentication.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SigninScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { email ->
                        coroutineScope.launch {
                            viewModel.sendIntent(LoginIntent.EnterEmail(email))
                        }
                    },
                    label = { Text("Email") },
                    isError = !state.isEmailValid && state.email.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (!state.isEmailValid && state.email.isNotEmpty()) {
                    Text(
                        text = "Invalid email",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { password ->
                        coroutineScope.launch {
                            viewModel.sendIntent(LoginIntent.EnterPassword(password))
                        }
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = !state.isPasswordValid && state.password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (!state.isPasswordValid && state.password.isNotEmpty()) {
                    Text(
                        text = "Password must be at least 8 characters",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                CommonButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.sendIntent(LoginIntent.Submit)
                        }
                    },
                    text = "Sign In",
                    isLoading = state.isLoading,
                    enabled = state.canSubmit
                )

                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error.getUserMessage(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { navController.navigate("signup") }
                ) {
                    Text("Don't have an account? Sign Up")
                }

                // On success, navigate (observe in ViewModel or here)
                LaunchedEffect(state.user) {
                    state.user?.let {
                        navController.navigate("user_dashboard") { popUpTo("auth") { inclusive = true } }
                    }
                }
            }
        }
    }
}