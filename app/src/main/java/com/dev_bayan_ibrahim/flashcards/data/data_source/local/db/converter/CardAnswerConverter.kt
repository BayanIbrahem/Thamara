package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter

import androidx.room.TypeConverter
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswerSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val json: Json = Json {
    explicitNulls = true
    ignoreUnknownKeys = true
}
object CardAnswerConverter {
    @TypeConverter
    fun serializeCardAnswer(cardAnswer: CardAnswer): String = json.encodeToString(
        serializer = CardAnswerSerializer,
        value = cardAnswer
    )

    @TypeConverter
    fun deserializeCardAnswer(string: String): CardAnswer = json.decodeFromString(
        deserializer = CardAnswerSerializer,
        string = string
    )
}