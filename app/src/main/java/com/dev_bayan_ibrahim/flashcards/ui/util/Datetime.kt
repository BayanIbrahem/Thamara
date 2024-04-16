package com.dev_bayan_ibrahim.flashcards.ui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun Instant.toCurrentDatetime() = toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.asSimpleDate(delimiter: Char = '-'): String {
    val datetime = toCurrentDatetime()
    val now = Clock.System.now().toCurrentDatetime()
    val showYear = datetime.year != now.year
    return datetime.date.run {
        buildString {
            if (showYear) {
                append(year)
                append(delimiter)
            }
            append(month.value)
            append(delimiter)
            append(dayOfMonth)
        }
    }
}

fun Instant.daysCount() = toCurrentDatetime().date.toEpochDays()

fun Instant.Companion.fromEpochDays(days: Int): Instant = Instant.fromEpochSeconds(days * 86_400L)