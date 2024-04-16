package com.dev_bayan_ibrahim.flashcards.ui.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun <T> Collection<T>.groupByDay(mapDate: (T) -> Instant): Map<Int, List<T>> = groupBy {
    mapDate(it).toLocalDateTime(
        TimeZone.currentSystemDefault()
    ).date.toEpochDays()
}