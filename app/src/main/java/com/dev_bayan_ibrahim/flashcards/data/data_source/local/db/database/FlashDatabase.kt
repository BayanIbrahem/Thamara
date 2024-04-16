package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.RankDao
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank

@Database(
    entities = [DeckHeader::class, Card::class, DeckPlay::class, CardPlay::class, UserRank::class],
    version = 4,
    exportSchema = false,
)
abstract class FlashDatabase : RoomDatabase() {
    abstract fun getCardDao(): CardDao
    abstract fun getDeckDao(): DeckDao
    abstract fun getDeckPlayDao(): DeckPlayDao
    abstract fun getCardPlayDao(): CardPlayDao
    abstract fun getRankDao(): RankDao
}