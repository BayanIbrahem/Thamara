package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

inline fun <T, R> Result<List<T>>.foldCollection(
    onSuccess: (value: List<T>) -> R,
    onEmpty: () -> R,
    onFailure: (exception: Throwable) -> R
): R {
    return fold(
        onSuccess = {
            if (it.isNotEmpty()) onSuccess(it) else onEmpty()
        },
        onFailure = onFailure
    )
}


