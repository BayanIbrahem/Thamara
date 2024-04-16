package com.dev_bayan_ibrahim.flashcards.data.model.deck

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter.InstantConverter
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter.StringListConverter
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/*
{
            "id": 1,
            "name": "test",
            "image_url": "test",
            "color": "test",
            "level": "e",
            "version": 0,
            "tags": [],
            "cards": [],
            "ratesCounts": 0,
            "rate": null
        }

 */
@Entity(tableName = "decks")
@TypeConverters(StringListConverter::class, InstantConverter::class)
@Serializable
data class DeckHeader(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val version: Int = 1,
    val tags: List<String> = emptyList(),
    val collection: String? = null,
    val name: String = "",
    val cardsCount: Int = 0,
    val pattern: String? = null,
    val color: Int = 0xFFFFFFFF.toInt(),
    val level: Int = 0,
    val rates: Int = 0, // rates count
    val rate: Float = 0f, // average rate
    val allowShuffle : Boolean = true,
    val creation: Instant = Clock.System.now(),
    val downloadInProgress: Boolean = false,
    val offlineData: Boolean = false,
    val offlineImages: Boolean = false,
)

val DeckHeader.colorAccent: Color get() = Color(color)

fun String.split(): List<String> = split(", ")