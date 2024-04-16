package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result

import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputField


fun <T> Result<OutputField<T>>.results(): Result<List<T>> = map { it.results }