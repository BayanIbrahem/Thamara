package com.dev_bayan_ibrahim.flashcards.data.util

import kotlin.math.roundToInt


/**
 * @return 0..1
 */
fun IntRange.percentOf(value: Int, coerce: Boolean = true): Float {
    val percent = (value.toFloat() - first) / (last - first)
    return if (coerce) {
        percent.coerceIn(0f..1f)
    } else percent
}

fun Float.percentValueOfRange(range: IntRange, coerce: Boolean): Int {
    val value = ((range.last - range.first) * this).roundToInt()
    return if (coerce) {
        value.coerceIn(range)
    } else {
        value
    }
}

fun Float.percentValueOfRange(range: ClosedFloatingPointRange<Float>, coerce: Boolean = true): Float {
    val value = (range.endInclusive - range.start) * this
    return if (coerce) {
        value.coerceIn(range)
    } else {
        value
    }
}
