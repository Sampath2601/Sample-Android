package com.example.sample.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(token: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(10.dp))

        // Display the auth token
        Text("Your Token:")
        Text(token)

        Spacer(Modifier.height(20.dp))

        // Logout action
        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}
