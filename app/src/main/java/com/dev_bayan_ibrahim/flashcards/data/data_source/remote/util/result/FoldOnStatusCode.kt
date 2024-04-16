package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

import io.ktor.client.plugins.ResponseException

/**
 * @param codes pass any codes you want to get a special response for them
 * @param onSuccess on success
 * @param onFailure on failure with an exception not a [ResponseException] or it is a [ResponseException]
 * with a code not between [codes]
 * @param onFailureWithCode if the exception was a response exception with a code between [codes]
 */
inline fun <T, R> Result<T>.foldOnStatusCode(
    vararg codes: Int,
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
    onFailureWithCode: (exception: ResponseException) -> R,
    finally: (result: R) -> Unit = {},
): R {
    return foldWithFinally(
        onSuccess = onSuccess,
        onFailure = {
            if (it is ResponseException && it.response.status.value in codes) {
                onFailureWithCode(it)
            } else {
                onFailure(it)
            }
        },
        finally = finally,
    )
}


