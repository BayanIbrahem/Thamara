package com.dev_bayan_ibrahim.flashcards.ui.app.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject


/**
 * an implementation of [NetworkMonitor] that uses the system service [ConnectivityManager]
 * to report internet availability.
 * */
class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext
    private val context: Context
) : NetworkMonitor {
    // callbackFlow creates a cold flow so data emission only starts when a collector
    // is collecting from the flow, callbackFlow is important here because it allows us
    // to create custom emission logic, turn asynchronous APIs that provide data through
    // callbacks into reactive streams to collect from using coroutines and to make the
    // flow react to events like: location changes, network availability, or user input.
    override val isOnline: Flow<Boolean> = callbackFlow {
        // get an instance of the system service [connectivity manager], we will send requests to
        // listen to network status and capabilities of the device this includes the networks that
        // the device is connected to and if a network has internet access.
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        // if unable to retrieve the connectivity manager then we can't monitor network status
        // and internet is most likely not available so we emit false and close the callback flow
        if(connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }
        // if able to get connectivity manager, create the network callback.
        // inside the callback get all available networks and add them to a set
        val networkCallback = object : NetworkCallback() {
            private val _networks = mutableSetOf<Network>()
            // if there is at least one network that is available
            // then we will assume that an internet connection is available
            // this can be useful if for some reason checking for internet availability fails
            // the real checking for internet availability starts later
            override fun onAvailable(network: Network) {
                _networks += network
                channel.trySend(true)
            }
            // when a network is disconnected, remove it from the set of connected network
            // and if there is at least one network available then we will assume that
            // an internet connection is available
            // this can be useful if for some reason checking for internet availability fails
            override fun onLost(network: Network) {
                _networks -= network
                channel.trySend(_networks.isNotEmpty())
            }
        }
        // create the real request to check for internet availability
        val internetCapabilityRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        // register the request and the previous network callback in the connectivity manager
        connectivityManager.registerNetworkCallback(internetCapabilityRequest, networkCallback)
        // now emit if there is actually an internet connection
        channel.trySend(connectivityManager.isCurrentlyConnected())
        // await close is used to do clean ups when the flow is cancelled, in our case
        // when the flow is cancelled we should unregister the callback from the connectivity manager
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
        // each collector of a cold flow, receives the entire emission of data, but in our case
        // we only want to collect the latest isOnline status so we use conflate(), this function
        // is used on a cold flow so the collector only collects the latest data emission.
        .conflate()

    @Suppress("DEPRECATION")
    private fun ConnectivityManager.isCurrentlyConnected(): Boolean = when {
        Build.VERSION.SDK_INT >= VERSION_CODES.M ->
            activeNetwork
                ?.let(::getNetworkCapabilities)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        else -> activeNetworkInfo?.isConnected
    } ?: false
}