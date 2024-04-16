package com.dev_bayan_ibrahim.flashcards.data.exception

import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.net.UnknownHostException

data object OfflineException: IllegalAccessException("No Internet connection") {
    private fun readResolve(): Any = OfflineException
}

suspend fun <T> Result<T>.mapUnknownErrorIfOffline(
    connectivityManager: Flow<Boolean>
): Result<T> = fold(
    onSuccess = { this },
    onFailure = {
        if (it is UnknownHostException || !connectivityManager.first()) {
            Result.failure(OfflineException)
        } else {
            this
        }
    }
)