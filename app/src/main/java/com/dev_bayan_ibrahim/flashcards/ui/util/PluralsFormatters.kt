package com.dev_bayan_ibrahim.flashcards.ui.util

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource


@Composable
fun flashPlurals(
    @PluralsRes
    id: Int,
    count: Int,
): String = pluralStringResource(
    id = id,
    count = count,
    count,
)

@Composable
fun Int.asFlashPlural(
    @PluralsRes
    id: Int
): String = flashPlurals(
    count = this,
    id = id
)