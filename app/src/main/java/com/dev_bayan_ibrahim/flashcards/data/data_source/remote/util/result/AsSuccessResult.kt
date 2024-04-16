package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result


fun <T> T.asSuccessResult(): Result<T> = Result.success(this)