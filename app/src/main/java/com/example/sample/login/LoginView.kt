package com.example.sample.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sample.TestTags

@Composable
fun LoginView(
    uiState: LoginUiState,
    onUsername: (String) -> Unit,
    onPassword: (String) -> Unit,
    onRemember: (Boolean) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        if (uiState.isOffline) {
            Text("You're offline", color = Color.Red)
            Spacer(Modifier.height(10.dp))
        }

        OutlinedTextField(
            value = uiState.username,
            onValueChange = onUsername,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
                .testTag(TestTags.USERNAME)

        )
        uiState.usernameError?.let {
            Text(it, color = Color.Red, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPassword,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
                .testTag(TestTags.PASSWORD)
        )
        uiState.passwordError?.let {
            Text(it, color = Color.Red, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRemember(!uiState.rememberMe) }
                .padding(vertical = 8.dp)
                .testTag(TestTags.REMEMBER)
        ) {
            Text("Remember me", modifier = Modifier.weight(1f))
            Switch(
                checked = uiState.rememberMe,
                onCheckedChange = onRemember
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLogin,
            enabled = uiState.isButtonEnabled && !uiState.lockedOut,
            modifier = Modifier.fillMaxWidth()
                .testTag(TestTags.LOGIN_BUTTON)
        ) {
            Text("Login")
        }

        uiState.errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(it,
                color = Color.Red,
                modifier = Modifier.testTag(TestTags.ERROR_TEXT)
            )
        }
    }
}
