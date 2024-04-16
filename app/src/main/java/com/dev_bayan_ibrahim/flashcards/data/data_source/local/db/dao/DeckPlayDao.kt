package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckWithCardsPlay
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckPlayDao {

    @Insert()
    fun insertDeckPlay(deck: DeckPlay): Long
    @Query("""
        select count(*) from deck_play
    """)
    fun getDeckPlaysCount(): Flow<Int>

    @Query("""
        select count(*) == 0 from deck_play where id = :id
    """)
    suspend fun checkDeckFirstPlay(id: Long): Boolean


    @Query(
        """
        select *
        from deck_play
        order by datetime
    """
    )
    suspend fun getAllPlays(): List<DeckWithCardsPlay>
}