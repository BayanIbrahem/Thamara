package com.dev_bayan_ibrahim.flashcards.data.util

import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf

data class DecksFilter(
    val tags: PersistentSet<String> = persistentSetOf(),
    val collections: PersistentSet<String> = persistentSetOf(),
    val levels: IntRange? = null,
    val rate: ClosedFloatingPointRange<Float>? = null,
) {
    fun applyOn(list: Iterable<DeckHeader>): List<DeckHeader> = list.filter {
        (it.collection in collections || collections.isEmpty())
                && (it.tags.any { it in tags } || tags.isEmpty())
                && levels?.contains(it.level) ?: true
                && rate?.contains(it.rate) ?: true
    }

    override fun hashCode(): Int {
        var result = tags.hashCode()
        result = 31 * result + (collections.hashCode())
        result = 31 * result + (levels?.hashCode() ?: 0)
        result = 31 * result + (rate?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DecksFilter) return false

        if (collections != other.collections) return false
        if (tags != other.tags) return false
        if (levels != other.levels) return false
        if (rate != other.rate) return false

        return true
    }
}

fun Iterable<DeckHeader>.applyDecksFilter(
    filter: DecksFilter?
): List<DeckHeader> = filter?.applyOn(this) ?: toList()