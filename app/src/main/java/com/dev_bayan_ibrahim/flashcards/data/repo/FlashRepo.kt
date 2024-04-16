package com.dev_bayan_ibrahim.flashcards.data.repo

import androidx.paging.PagingData
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckWithCardsPlay
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import kotlinx.coroutines.flow.Flow

interface FlashRepo {
    // Home
    fun getTotalPlaysCount(): Flow<Int>
    fun getUser(): Flow<User?>
    fun getGeneralStatistics(): Flow<GeneralStatistics>
    suspend fun setUser (name: String, age: Int)

    fun getLibraryDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Flow<Map<DecksGroup, List<DeckHeader>>>

    suspend fun getBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Result<List<DeckHeader>>
    suspend fun getPaginatedBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Flow<PagingData<DeckHeader>>

    suspend fun getDeckCards(id: Long): Deck

    suspend fun saveDeckResults(
        id: Long,
        cardsResults: Map<Long, Boolean> // answer status
    )

    fun initializedDb(): Flow<Boolean>

    fun getDatabaseInfo(): Flow<DecksDatabaseInfo>
    suspend fun isFirstPlay(id: Long): Boolean
    suspend fun updateUserRank(newRank: UserRank)
    suspend fun saveDeckToLibrary(
        deck: Deck,
        downloadImages: Boolean,
    ): Flow<DownloadStatus>

    suspend fun rateDeck(
        id: Long,
        rate: Int,
        deviceId: String
    ): Result<DeckHeader>

    suspend fun getDeckInfo(id: Long): Result<Deck>
    fun getLibraryDecksIds(): Flow<Map<Long, Boolean>>
    suspend fun deleteDeckWithCards(id: Long)
    suspend fun deleteDeckImages(id: Long)
    fun downloadDeckImages(deck: Deck): Flow<DownloadStatus>
    suspend fun downloadDeckImages(id: Long): Flow<DownloadStatus>
    suspend fun getRankChangesStatistics(): List<UserRank>
    suspend fun getPlaysStatistics(): List<DeckWithCardsPlay>
    suspend fun getLeveledDecksCount(): List<Pair<Int, Int>>
    suspend fun getAllTags(): Result<List<String>>
    suspend fun getAllCollections(): Result<List<String>>
    suspend fun List<DeckHeader>.applyDecksFilters(
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Map<DecksGroup, List<DeckHeader>>
}