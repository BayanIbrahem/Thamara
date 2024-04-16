package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.data.exception.DeckDeserializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

object DeckHeaderSerializer : KSerializer<DeckHeader> {
    override val descriptor: SerialDescriptor = buildDeckDescriptor(
        "DeckHeaderSerializer",
        true
    )

    override fun deserialize(decoder: Decoder): DeckHeader = deserializeDeck(
        decoder = decoder,
        descriptor = descriptor,
        headerOnly = true
    ).header

    override fun serialize(
        encoder: Encoder,
        value: DeckHeader
    ) {
    }
}

object DeckHeaderRateSerializer : KSerializer<DeckHeader> {
    override val descriptor: SerialDescriptor = buildRateDeckDescriptor()
    override fun deserialize(decoder: Decoder): DeckHeader {
        var id: Long? = null
        var rates: Int? = null // rates count
        var rate: Float? = null // average rate

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> rates = decodeIntElement(descriptor, index)
                    2 -> rate = decodeNullablePrimitive(
                        descriptor,
                        index,
                    ) ?: 0f

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }

        return DeckHeader(
            id = id ?: throw DeckDeserializationException("null id"),
            version = 0,
            tags = emptyList(),
            collection = "",
            name = "",
            cardsCount = 0,
            pattern = "",
            color = 0,
            level = 0,
            rates = rates ?: 0,
            rate = rate ?: 0f,
        )
    }

    override fun serialize(encoder: Encoder, value: DeckHeader) {}
}

object CollectionSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "CollectionSerializer"
    ) {
        element<Long>("id")
        element<String>("name")
    }

    override fun deserialize(decoder: Decoder): String {
        var id: Long? = null
        var name: String? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> name = decodeStringElement(descriptor, index)

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }

        }

        return name!!
    }

    override fun serialize(encoder: Encoder, value: String) {}
}


object TagSerializer1 : KSerializer<String> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "TagSerializer"
    ) {
        element<Long>("id")
        element<String>("value")
    }

    override fun deserialize(decoder: Decoder): String {
        var id: Long? = null
        var name: String? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> name = decodeStringElement(descriptor, index)

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }

        }

        return name!!
    }

    override fun serialize(encoder: Encoder, value: String) {}
}
object TagSerializer2 : KSerializer<String> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "TagSerializer2"
    ) {
        element<Long>("deck_id")
        element<Long>("tag_id")
    }

    override fun deserialize(decoder: Decoder): String {
        var id: Long? = null
        var name: String? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> name = decodeLongElement(descriptor, index).toString()

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }

        }

        return name!!
    }

    override fun serialize(encoder: Encoder, value: String) {}
}
