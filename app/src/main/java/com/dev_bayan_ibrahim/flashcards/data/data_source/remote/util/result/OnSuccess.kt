package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

inline fun <T> Result<T>.onSuccess(
    body: (T) -> Unit
): Result<T> {
    getOrNull()?.let(body)
    return this
}


