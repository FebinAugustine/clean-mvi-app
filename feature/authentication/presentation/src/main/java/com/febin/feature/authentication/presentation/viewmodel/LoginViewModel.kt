package com.febin.feature.authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.feature.authentication.domain.usecase.LoginUseCase
import com.febin.feature.authentication.presentation.intent.LoginIntent
import com.febin.feature.authentication.presentation.state.LoginState
import com.febin.feature.authentication.domain.model.LoginError
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.exceptionOrNull
import com.febin.shared_domain.model.getOrNull
import com.febin.shared_domain.model.isLoading
import com.febin.shared_domain.model.isSuccess
import com.febin.shared_domain.usecase.*  // Imports extensions (isLoading, isSuccess, getOrNull, exceptionOrNull)
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVI ViewModel for Login.
 * - Handles intents, calls use case, updates state.
 * - Injected via Koin with LoginUseCase.
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _intent = Channel<LoginIntent>(Channel.BUFFERED)
    val intent = _intent.receiveAsFlow()

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                when (intent) {
                    is LoginIntent.EnterEmail -> _state.value = _state.value.copy(email = intent.email)
                    is LoginIntent.EnterPassword -> _state.value = _state.value.copy(password = intent.password)
                    is LoginIntent.ClearError -> _state.value = _state.value.copy(error = null)
                    is LoginIntent.Submit -> performLogin()
                }
            }
        }
    }

    private fun performLogin() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            loginUseCase(_state.value.email, _state.value.password).collectLatest { result ->
                when {
                    result.isLoading() -> _state.value = _state.value.copy(isLoading = true)
                    result.isSuccess() -> _state.value = _state.value.copy(
                        isLoading = false,
                        user = result.getOrNull(),
                        error = null
                    )
                    else -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull() as? LoginError
                    )
                }
            }
        }
    }

    /**
     * Public function to send intents from UI (e.g., screens).
     * - Wraps Channel.send for suspend-safety.
     */
    fun sendIntent(intent: LoginIntent) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }
}