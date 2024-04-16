package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.onlinePatterns
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.patterns
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardSerializer
import com.dev_bayan_ibrahim.flashcards.data.model.card.ColorHexSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.serializer


@OptIn(InternalSerializationApi::class)
fun buildDeckDescriptor(
    serialName: String,
    headerOnly: Boolean,
) = buildClassSerialDescriptor(
    serialName
) {
    element<Long>("id")
    element<Int>("version")
    element("tags", ListSerializer(String::class.serializer()).descriptor)
    element("collection", TagSerializer2.descriptor)

    element<String>("name")
    element<String>("image_url")
    element("color", ColorHexSerializer.descriptor)
    element<Int>("level")

    element<Int>("ratesCount")
    element<Float>("rate")
    element<Boolean>("allowShuffle")
    if (headerOnly) {
        element<Int>("cardsCount")
    } else {
        element("cards", ListSerializer(CardSerializer).descriptor)
    }
}


@OptIn(InternalSerializationApi::class)
fun buildRateDeckDescriptor(
) = buildClassSerialDescriptor(
    "buildRateDeckDescriptor"
) {
    element<Long>("id")
    element<Int>("ratesCount")
    element<Float>("rate")
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
fun deserializeDeck(
    decoder: Decoder,
    descriptor: SerialDescriptor,
    headerOnly: Boolean,
): Deck {
    var id: Long? = null
    var version: Int? = null
    val tags = mutableListOf<String>()
    var collection: String? = null

    var name: String? = null
    var pattern: String? = null // url for an image
    var color: Int? = null
    var level: Int? = null

    var rates: Int? = null // rates count
    var rate: Float? = null // average rate
    var allowShuffle: Boolean? = null
    var cardsCount: Int? = null
    val cards = mutableListOf<Card>()

    decoder.decodeStructure(descriptor) {
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> id = decodeLongElement(descriptor, index)
                1 -> version = decodeIntElement(descriptor, index)
                2 -> tags.addAll(
                    decodeSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = ListSerializer(String::class.serializer())
                    )
                )

                3 -> collection = decodeNullableSerializableElement(
                    descriptor = descriptor,
                    index = index,
                    deserializer = CollectionSerializer
                )

                4 -> name = decodeStringElement(descriptor, index)

                5 -> pattern = decodeNullablePrimitive<String>(descriptor, index)?.ifBlank { null }
                6 -> color = decodeSerializableElement(descriptor, index, ColorHexSerializer)
                7 -> level = decodeIntElement(descriptor, index)
                8 -> rates = decodeIntElement(descriptor, index)
                9 -> rate = decodeNullableSerializableElement(
                    descriptor,
                    index,
                    Float::class.serializer()
                ) ?: 0f

                10 -> allowShuffle = decodeBooleanElement(descriptor, index)
                11 -> {
                    if (headerOnly) {
                        cardsCount = decodeIntElement(descriptor, index)
                    } else {
                        cards.addAll(
                            decodeSerializableElement(
                                descriptor = descriptor,
                                index = index,
                                deserializer = ListSerializer(CardSerializer)
                            )
                        )

                    }
                }

                CompositeDecoder.DECODE_DONE -> break
                else -> error("unexpected index $index")
            }
        }
    }

    return Deck(
        DeckHeader(
            id = id ?: throw DeckDeserializationException("null id"),
            version = version ?: throw DeckDeserializationException("null version"),
            tags = tags,
            collection = collection,
            name = name ?: throw DeckDeserializationException("null name"),
            cardsCount = if (headerOnly) cardsCount
                ?: throw DeckDeserializationException("null cards count") else cards.count(),
            pattern = pattern,
            color = color ?: throw DeckDeserializationException("null color"),
            level = level ?: throw DeckDeserializationException("null level"),
            rates = rates ?: 0,
            rate = rate ?: 0f,
            allowShuffle = allowShuffle ?: false,
        ), cards
    )
}

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
inline fun <reified T : Any> CompositeDecoder.decodeNullablePrimitive(
    descriptor: SerialDescriptor,
    index: Int
): T? = decodeNullableSerializableElement(
    descriptor = descriptor,
    index = index,
    deserializer = T::class.serializer()
)