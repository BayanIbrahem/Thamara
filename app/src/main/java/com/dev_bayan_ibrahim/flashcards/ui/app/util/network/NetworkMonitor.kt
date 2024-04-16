package com.dev_bayan_ibrahim.flashcards.ui.app.util.network

import kotlinx.coroutines.flow.Flow

/**
 * a simple interface to monitor app connectivity status
 * @property isOnline emits true when the internet is available and false otherwise.
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}