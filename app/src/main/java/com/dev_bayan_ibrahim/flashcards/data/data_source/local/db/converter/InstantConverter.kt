package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object InstantConverter {
    @TypeConverter
    fun instantToLong(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long): Instant = Instant.fromEpochMilliseconds(value)
}