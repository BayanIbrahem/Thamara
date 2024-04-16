package com.dev_bayan_ibrahim.flashcards.data.model.deck

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DeckSerializer : KSerializer<Deck> {
    override val descriptor: SerialDescriptor = buildDeckDescriptor(
        serialName = "DeckSerializer",
        headerOnly = false,
    )

    override fun deserialize(decoder: Decoder): Deck = deserializeDeck(
        decoder = decoder,
        descriptor = descriptor,
        headerOnly = false
    )

    override fun serialize(
        encoder: Encoder,
        value: Deck
    ) {
    }
}