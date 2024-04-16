package com.dev_bayan_ibrahim.flashcards.data.model.card

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter.CardAnswerConverter
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import kotlin.time.Duration.Companion.days

@Entity(
    tableName = "card",
    foreignKeys = [
        ForeignKey(
            entity = DeckHeader::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("deckId")]
)
@TypeConverters(CardAnswerConverter::class)
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val deckId: Long,
    val index: Int = 0,
    val question: String = "",
    val image: String = "",
    val answer: CardAnswer = CardAnswer.Info(1.days)
)

fun List<Card>.fixCardsIndexes(): List<Card> = mapIndexed { index, card -> card.copy(index = index) }