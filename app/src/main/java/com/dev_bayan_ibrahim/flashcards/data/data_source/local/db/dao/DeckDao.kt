package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao  {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDecks(decks: List<DeckHeader>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(decks: DeckHeader)

    @Query("""
        update decks set offlineImages = :offline where id = :id
    """)
    suspend fun updateDeckOfflineImages(id: Long, offline: Boolean)

    @Query("""
        update decks set rate = :rate, rates = :rates where id = :id
    """)
    suspend fun updateDeckRate(id: Long, rate: Float, rates: Int)

    @Query("""
        select * from decks where id = :id
    """)
    suspend fun getDeck(id: Long): DeckHeader?

    @Query(
        """
        update decks set downloadInProgress = 0 where id = :id
    """
    )
    suspend fun finishDownloadDeck(id: Long)

    @Query("""
        select id from decks where downloadInProgress
    """)
    suspend fun getDownloadingDecks(): List<Long>
    @Query("""
        delete from decks where downloadInProgress
    """)
    suspend fun deleteDownloadingDecks()
    @Query("""
        delete from decks where id = :id
    """)
    suspend fun deleteDeck(id: Long)

    @Query("""
        select tags from decks
    """)
    fun getTags(): Flow<List<String>>
    @Query("""
        select collection from decks
    """)
    fun getCollections(): Flow<List<String>>

    @Query("""
        select * from decks where name like :query
    """)
    fun getDecks(query: String): Flow<List<DeckHeader>>
    @Query("""
        select * from decks where not downloadInProgress  
    """)
    fun getDownloadedDecks(): Flow<List<DeckHeader>>
    @Query("""
        select count(*) from decks
    """)
    fun getDecksCount(): Flow<Int>
    @Query("""
        select count(*) from decks where creation >= :startTimeStamp
    """)
    fun getDecksCountAfter(startTimeStamp: Long): Flow<Int>
}
