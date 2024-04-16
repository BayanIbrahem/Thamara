package com.dev_bayan_ibrahim.flashcards.data.model.play

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

data class DeckWithCardsPlay(
    @Embedded
    val deckPlay: DeckPlay,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckPlayId"
    )
    val cardsPlay: List<CardPlay>
) {
    @Ignore
    val failedPlays = cardsPlay.count { it.failed }
    @Ignore
    val successPlays = cardsPlay.count() - failedPlays
}
