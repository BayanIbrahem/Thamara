package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

inline fun <T, R> Result<T>.foldWithFinally(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
    finally: (result: R) -> Unit,
): R {
    return fold(
        onSuccess = onSuccess,
        onFailure = onFailure
    ).also(finally)
}


