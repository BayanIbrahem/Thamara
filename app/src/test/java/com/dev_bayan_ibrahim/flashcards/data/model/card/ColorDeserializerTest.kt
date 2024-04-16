package com.dev_bayan_ibrahim.flashcards.data.model.card

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorDeserializerTest {

    private val requiredColor = Color(0xFF10f0e0)
    private val colorString = "10f0E0"
    private fun tryDeserializeColor(prefix: String) {
        val color = Json.decodeFromString(
            deserializer = ColorHexSerializer,
            string = buildString {
                append("\"")
                append(prefix)
                append(colorString)
                append("\"")
            }
        )
        val requiredArgb = requiredColor.toArgb()
        val argb = Color(color).toArgb()
        assertEquals(requiredArgb, argb)
    }

    @Test
    fun colorDeserializer_hash_6Digits() {
        tryDeserializeColor("#")
    }

    @Test
    fun colorDeserializer_0x_6Digits() {
        tryDeserializeColor("0x")
        tryDeserializeColor("0X")
    }

    @Test
    fun colorDeserializer_6Digits() {
        tryDeserializeColor("")
    }

    @Test
    fun colorDeserializer_hash_8Digits() {
        tryDeserializeColor("#FF")
    }

    @Test
    fun colorDeserializer_0x_8Digits() {
        tryDeserializeColor("0xFF")
        tryDeserializeColor("0XFF")
    }

    @Test
    fun colorDeserializer_8Digits() {
        tryDeserializeColor("FF")
    }
}