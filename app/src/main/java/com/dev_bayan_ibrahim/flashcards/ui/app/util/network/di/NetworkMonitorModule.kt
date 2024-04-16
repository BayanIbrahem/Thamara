package com.dev_bayan_ibrahim.flashcards.ui.app.util.network.di

import com.dev_bayan_ibrahim.flashcards.ui.app.util.network.ConnectivityManagerNetworkMonitor
import com.dev_bayan_ibrahim.flashcards.ui.app.util.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkMonitorModule {
    @Binds
    abstract fun bindsNetworkMonitor(
        impl: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}