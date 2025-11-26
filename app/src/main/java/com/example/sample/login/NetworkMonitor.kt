package com.example.sample.login

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}
