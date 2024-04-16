package com.dev_bayan_ibrahim.flashcards.data.util

import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader

enum class DecksOrderType(
    override val label: Int,
    override val icon: Int?

) : FlashSelectableItem {
    NAME(R.string.name, null),
    RATE(R.string.rate, null),
    LEVEL(R.string.level, null);

    fun toDeckOrder(asc: Boolean): DecksOrder = when (this) {
        NAME -> DecksOrder.Name(asc)
        RATE -> DecksOrder.Rate(asc)
        LEVEL -> DecksOrder.Level(asc)
    }
}

sealed interface DecksOrder {
    val asc: Boolean
    val type: DecksOrderType

    data class Name(override val asc: Boolean) : DecksOrder {
        override val type = DecksOrderType.NAME
    }

    data class Rate(override val asc: Boolean) : DecksOrder {
        override val type = DecksOrderType.RATE
    }

    data class Level(override val asc: Boolean) : DecksOrder {
        override val type = DecksOrderType.LEVEL
    }

    fun applyOn(list: Iterable<DeckHeader>): List<DeckHeader> = when (this) {
        is Level -> if (asc) {
            list.sortedBy { it.level }
        } else {
            list.sortedByDescending { it.level }
        }

        is Name -> if (asc) {
            list.sortedBy { it.name }
        } else {
            list.sortedByDescending { it.name }
        }


        is Rate -> if (asc) {
            list.sortedBy { it.rate }
        } else {
            list.sortedByDescending { it.rate }
        }
    }
}

fun Iterable<DeckHeader>.applyDecksOrder(
    order: DecksOrder?,
): List<DeckHeader> = order?.applyOn(this) ?: toList()
