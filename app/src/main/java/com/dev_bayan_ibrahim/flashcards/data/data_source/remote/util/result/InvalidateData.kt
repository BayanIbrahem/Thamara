package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

inline fun <T> Result<T>.validate(
    validator: (T) -> Throwable?
): Result<T> {
    return fold(
        onSuccess = {
            val error = validator(it)
            if (error != null) Result.failure(error) else this
        },
        onFailure = { this }
    )
}
