package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import kotlinx.coroutines.flow.Flow

@Dao
interface CardPlayDao {

    @Insert
    fun insertAllCards(cards: List<CardPlay>)

    // percent of accuracy
    @Query("""
        select count(*) * 100.0 / (select max(count(*), 1) from card_play)
        from card_play 
        where not failed
    """)
    fun getCardsAccuracyAverage(): Flow<Float>

    @Query("""
        select count(*) 
        from card_play 
        where deckPlayId in (select id from deck_play where datetime >= :startTimeStamp)
    """)
    fun getPlaysCountAfter(startTimeStamp: Long): Flow<Int>

    @Query(
        """
        select count(*) 
        from card_play
        where 
            deckPlayId in (select id from deck_play where datetime >= :startTimeStamp)
            and failed = :failed
    """
    )
    fun getAnswersOf(startTimeStamp: Long, failed: Boolean): Flow<Int>

}