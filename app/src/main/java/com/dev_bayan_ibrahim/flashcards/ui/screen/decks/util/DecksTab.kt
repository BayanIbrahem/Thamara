package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R

enum class DecksTab (
    @StringRes
    val nameRes: Int
) {
    LIBRARY(R.string.library),
    BROWSE(R.string.browse),
}