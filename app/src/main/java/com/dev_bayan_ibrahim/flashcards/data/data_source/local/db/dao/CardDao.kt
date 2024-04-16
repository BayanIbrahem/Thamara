package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCards(cards: List<Card>)

    @Query(
        """
        select * from card where deckId = :deckId
    """
    )
    suspend fun getCards(deckId: Long): List<Card>

    @Query(
        """
        select count(*) from card
    """
    )
    fun getCardsCount(): Flow<Int>

    @Query(
        """
            delete from card where deckId = :id
    """
    )
    fun deleteCardsOfDeck(id: Long)

    @Query(
        """
        select count(*) from card where deckId in (select id from decks where creation >= :startTimeStamp)
    """
    )
    fun getCardsCountAfter(startTimeStamp: Long): Flow<Int>
}
