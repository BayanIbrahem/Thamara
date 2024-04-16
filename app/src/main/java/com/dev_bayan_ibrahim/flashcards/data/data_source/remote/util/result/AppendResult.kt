package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

/**
 * @return success if both of them are success or the first response error if it is error or second
 * response error if it is error
 */
fun <T1, T2> Result<T1>.appendResult(
    result: Result<T2>
): Result<Pair<T1, T2>> {
    return fold(
        onSuccess = { data1 ->
            result.fold(
                onSuccess = { data2 ->
                    Result.success(data1 to data2)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        },
        onFailure = {
            Result.failure(it)
        }
    )
}