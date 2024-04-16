package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.data.model.card.Card

const val INVALID_ID = -1L
data class Deck(
//    @Embedded
    val header: DeckHeader = DeckHeader(INVALID_ID),
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "deckId"
//    )
    val cards: List<Card> = emptyList(),
)