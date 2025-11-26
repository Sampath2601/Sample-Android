package com.example.sample.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,

    val usernameError: String? = null,
    val passwordError: String? = null,

    val isButtonEnabled: Boolean = false,
    val failureCount: Int = 0,
    val lockedOut: Boolean = false,

    val errorMessage: String? = null,

    val navigateHome: Boolean = false,
    val authToken: String? = null,

    val isOffline: Boolean = false
)
