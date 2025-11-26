package com.example.sample.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.auth.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val repository: AuthRepository,
    private val networkMonitor: NetworkMonitor,
    private val tokenStorage: TokenStorage
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        // Check for Offline State
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { online ->
                    _uiState.update { it.copy(isOffline = !online) }
                }
        }
        // login if token exists
        tokenStorage.getToken()?.let { token ->
            _uiState.update {
                it.copy(
                    authToken = token,
                    navigateHome = true
                )
            }
        }

    }

    // Usernamefield
    fun onUsernameChanged(value: String) {
        val error =
            if (value.length > 10) "Invalid Username" else null

        val enable = error == null &&
                _uiState.value.passwordError == null &&
                value.isNotBlank() &&
                _uiState.value.password.isNotBlank()

        _uiState.update {
            it.copy(
                username = value,
                usernameError = error,
                isButtonEnabled = enable,
                errorMessage = null
            )
        }
    }

    // Passwordfield
    fun onPasswordChanged(value: String) {
        val error =
            if (value.length > 10) "Invalid Password" else null

        val enable = error == null &&
                _uiState.value.usernameError == null &&
                value.isNotBlank() &&
                _uiState.value.username.isNotBlank()

        _uiState.update {
            it.copy(
                password = value,
                passwordError = error,
                isButtonEnabled = enable,
                errorMessage = null
            )
        }
    }

    // Remember Me Toggle
    fun onRememberChanged(value: Boolean) {
        _uiState.update { it.copy(rememberMe = value) }
    }

    // Login functionality
    fun login() {
        val state = _uiState.value

        if (state.isOffline) {
            _uiState.update { it.copy(errorMessage = "You are offline") }
            return
        }

        if (state.lockedOut) {
            _uiState.update { it.copy(errorMessage = "Locked out after 3 failed attempts") }
            return
        }

        viewModelScope.launch {
            val result = repository.login(state.username, state.password)

            result.onSuccess { token ->
                if (state.rememberMe) {
                    tokenStorage.saveToken(token)   // <-- required for tests
                }

                _uiState.update {
                    it.copy(
                        navigateHome = true,
                        authToken = token,
                        errorMessage = null
                    )
                }
            }


            result.onFailure {
                val attempts = state.failureCount + 1
                val locked = attempts >= 3

                _uiState.update {
                    it.copy(
                        failureCount = attempts,
                        lockedOut = locked,
                        errorMessage = if (locked)
                            "Locked out after 3 failed attempts"
                        else
                            "Invalid credentials (${attempts}/3)"
                    )
                }
            }
        }
    }

    fun reset() {
        _uiState.value = LoginUiState()
    }

    fun logout() {
        tokenStorage.clear()
        _uiState.value = LoginUiState()
    }

}
