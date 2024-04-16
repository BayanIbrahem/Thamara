package com.dev_bayan_ibrahim.flashcards.data.model.play

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("card_play")
data class CardPlay(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val cardId: Long,
    val deckPlayId: Long,
    val failed: Boolean,
)

/*
{
    "id": 1,
    "cardId": 123,
    "deckPlayId": 456,
    "failed": false
}
 */