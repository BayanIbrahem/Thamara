package com.dev_bayan_ibrahim.flashcards.data.model.card

import com.dev_bayan_ibrahim.flashcards.data.exception.DeckInvalidColorException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private val validColorRegex = listOf(
    Regex("([0-9abcdefABCDEF]{8})"),
    Regex("([0-9abcdefABCDEF]{6})"),
)

object ColorHexSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ColorHexSerializer",
        PrimitiveKind.STRING
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun deserialize(decoder: Decoder): Int {
        val key = decoder.decodeString()

        val color = validColorRegex.firstNotNullOfOrNull {
            it.find(key)?.value?.run {
                if (length == 6) {
                    "FF$this"
                } else {
                    this
                }
            }
        } ?: throw DeckInvalidColorException(key)

        return color.uppercase().hexToInt(HexFormat.UpperCase)
    }

    override fun serialize(
        encoder: Encoder,
        value: Int
    ) {
    }
}