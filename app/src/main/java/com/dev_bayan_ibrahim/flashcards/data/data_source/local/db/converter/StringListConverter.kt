package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter

import androidx.room.TypeConverter

object StringListConverter {
    @TypeConverter
    fun listStringToString(list: List<String>): String = list.joinToString()

    @TypeConverter
    fun stringToListString(string: String): List<String> = string.split(", ")
}