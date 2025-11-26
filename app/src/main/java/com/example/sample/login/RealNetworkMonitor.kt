package com.example.sample.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@RequiresApi(Build.VERSION_CODES.N)
class RealNetworkMonitor(
    private val context: Context
) : NetworkMonitor {

    // Checks Device State
    override val isOnline: Flow<Boolean> = callbackFlow {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        trySend(isNetworkAvailable(cm))

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(isNetworkAvailable(cm))
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                trySend(isNetworkAvailable(cm))
            }
        }

        cm.registerDefaultNetworkCallback(callback)

        awaitClose { cm.unregisterNetworkCallback(callback) }
    }

    private fun isNetworkAvailable(cm: ConnectivityManager): Boolean {
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
