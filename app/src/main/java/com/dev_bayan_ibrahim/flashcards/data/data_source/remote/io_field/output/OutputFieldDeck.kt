package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckSerializer
import kotlinx.serialization.Serializable

@Serializable
data class OutputFieldDeck(
    val message: String,
    @Serializable(DeckSerializer::class)
    val deck: Deck
)