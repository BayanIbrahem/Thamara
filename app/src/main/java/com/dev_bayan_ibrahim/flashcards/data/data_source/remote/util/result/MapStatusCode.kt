package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

import io.ktor.client.plugins.ResponseException

/**
 * @param codes pass any codes you want to get a special response for them
 * @param onSuccess on success
 * @param onFailure on failure with an exception not a [ResponseException] or it is a [ResponseException]
 * with a code not between [codes]
 * @param onFailureWithCode if the exception was a response exception with a code between [codes]
 */
inline fun <T> Result<T>.mapStatusCode(
    vararg codes: Int,
    onFailureWithCode: (exception: ResponseException) -> T,
): Result<T> {
    return fold(
        onSuccess = { this },
        onFailure = {
            if (it is ResponseException && it.response.status.value in codes) {
                Result.success(onFailureWithCode(it))
            } else {
                this
            }
        },
    )
}


