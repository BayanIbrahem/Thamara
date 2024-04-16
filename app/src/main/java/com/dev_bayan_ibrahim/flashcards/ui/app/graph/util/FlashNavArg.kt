package com.dev_bayan_ibrahim.flashcards.ui.app.graph.util

data class FlashNavArg(
    val label: String,
    val value: String?,
)

fun Collection<String>.asNavArg(): Set<FlashNavArg> =
    this.map { FlashNavArg(it, "{$it}") }.toSet()

fun <T : Enum<T>> List<T>.asNavLabels(): Set<String> = this.map { it.name }.toSet()