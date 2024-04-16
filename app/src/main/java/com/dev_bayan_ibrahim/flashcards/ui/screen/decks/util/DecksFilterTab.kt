package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R

enum class DecksFilterTab (
    @StringRes
    val nameRes: Int,
    @DrawableRes
    val icon: Int,
) {
    Display(R.string.display, R.drawable.ic_sort),
    Tag(R.string.tag, R.drawable.ic_tag),
    Collection(R.string.collection, R.drawable.ic_collection),
}