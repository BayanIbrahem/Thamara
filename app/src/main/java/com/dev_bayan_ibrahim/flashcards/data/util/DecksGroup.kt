package com.dev_bayan_ibrahim.flashcards.data.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R

enum class DecksGroupType (
    @StringRes
    override val label: Int,
    @DrawableRes
    override val icon: Int?,
): FlashSelectableItem {
    COLLECTION(R.string.collection, null),
    TAG(R.string.tag, null),
    LEVEL(R.string.level, null),
    NONE(R.string.none, null),
}
sealed class DecksGroup(
    val name: String,
    val type: DecksGroupType,
) {
    data class Collection(
        val collection: String?
    ) : DecksGroup(
        name = collection ?: "",
        type = DecksGroupType.COLLECTION
    )

    data class Tag(val tag: String) : DecksGroup(
        name = tag,
        type = DecksGroupType.TAG
    )

    data class Level(val level: Int) : DecksGroup(
        name = level.toString(),
        type = DecksGroupType.LEVEL
    )

    data object None : DecksGroup(
        name = "",
        type = DecksGroupType.NONE
    )
}
