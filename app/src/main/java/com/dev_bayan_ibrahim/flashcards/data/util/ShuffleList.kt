package com.dev_bayan_ibrahim.flashcards.data.util

fun <T> List<T>.shuffle(): List<T> {
    if (isEmpty()) return emptyList()
    val indexes = (0 until count()).toMutableSet()
    val newItems = mutableListOf<T>()

    repeat(count()) {
        val index = indexes.random()
        indexes.remove(index)
        newItems.add(this[index])
    }

    return newItems
}