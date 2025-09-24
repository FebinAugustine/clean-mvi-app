package com.febin.feature.authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.feature.authentication.domain.usecase.SignupUseCase
import com.febin.feature.authentication.presentation.intent.SignupIntent
import com.febin.feature.authentication.presentation.state.SignupState
import com.febin.feature.authentication.domain.model.SignupError
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
 * MVI ViewModel for Signup.
 * - Similar to LoginViewModel; more fields.
 */
class SignupViewModel(
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state.asStateFlow()

    private val _intent = Channel<SignupIntent>(Channel.BUFFERED)
    val intent = _intent.receiveAsFlow()

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                when (intent) {
                    is SignupIntent.EnterFullName -> _state.value = _state.value.copy(fullName = intent.fullName)
                    is SignupIntent.EnterEmail -> _state.value = _state.value.copy(email = intent.email)
                    is SignupIntent.EnterPassword -> _state.value = _state.value.copy(password = intent.password)
                    is SignupIntent.EnterPhone -> _state.value = _state.value.copy(phone = intent.phone)
                    is SignupIntent.ClearError -> _state.value = _state.value.copy(error = null)
                    is SignupIntent.Submit -> performSignup()
                }
            }
        }
    }

    private fun performSignup() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            signupUseCase(
                _state.value.fullName,
                _state.value.email,
                _state.value.password,
                _state.value.phone
            ).collectLatest { result ->
                when {
                    result.isLoading() -> _state.value = _state.value.copy(isLoading = true)
                    result.isSuccess() -> _state.value = _state.value.copy(
                        isLoading = false,
                        user = result.getOrNull(),
                        error = null
                    )
                    else -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull() as? SignupError
                    )
                }
            }
        }
    }

    fun sendIntent(intent: SignupIntent) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }
}