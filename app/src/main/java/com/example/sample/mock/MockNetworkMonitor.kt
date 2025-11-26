package com.example.sample

import com.example.sample.login.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockNetworkMonitor : NetworkMonitor {

    // By default: device is online
    private val _isOnline = MutableStateFlow(true)

    override val isOnline: Flow<Boolean>
        get() = _isOnline

    /**
     * Use this in tests or UI to simulate offline/online state.
     */
    fun setOnline(value: Boolean) {
        _isOnline.value = value
    }
}
