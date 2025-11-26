package com.example.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sample.auth.TokenStorage
import com.example.sample.home.HomeScreen
import com.example.sample.login.AuthRepositoryImpl
import com.example.sample.login.LoginView
import com.example.sample.login.LoginViewModel
import com.example.sample.login.RealNetworkMonitor

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val repository = AuthRepositoryImpl()
            val networkMonitor = RealNetworkMonitor(application)
            val tokenStorage = TokenStorage(application)

            val viewModel: LoginViewModel by viewModels {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return LoginViewModel(
                            application,
                            repository,
                            networkMonitor,
                            tokenStorage
                        ) as T
                    }
                }
            }

            // Observe UI state
            val uiState by viewModel.uiState.collectAsState()

            // Token Persistance moves to homescreen
            if (uiState.navigateHome || tokenStorage.getToken() != null) {
                HomeScreen(
                    token = uiState.authToken.orEmpty(),
                    onLogout = { viewModel.logout() }
                )
            } else {
                // Show Login Screens
                LoginView(
                    uiState = uiState,
                    onUsername = viewModel::onUsernameChanged,
                    onPassword = viewModel::onPasswordChanged,
                    onRemember = viewModel::onRememberChanged,
                    onLogin = viewModel::login
                )
            }
        }
    }
}
