package com.dev_bayan_ibrahim.flashcards.data.repo

import androidx.paging.PagingData
import com.dev_bayan_ibrahim.flashcards.data.data_source.datastore.DataStoreManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.cardsInitialValues
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.decksInitialValue
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.generateLargeFakeCardsForDeck
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.generateLargeFakeDecks
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.RankDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database.FlashDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage.FlashFileManager
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckWithCardsPlay
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.shuffle
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.max_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.min_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.seconds

class FlashRepoFakeImpl(
    private val db: FlashDatabase,
    private val preferences: DataStoreManager,
    private val fileManager: FlashFileManager,
) : FlashRepo,
    DeckDao by db.getDeckDao(),
    CardDao by db.getCardDao(),
    DeckPlayDao by db.getDeckPlayDao(),
    CardPlayDao by db.getCardPlayDao(),
    RankDao by db.getRankDao() {

    private suspend inline fun <T> delayedResult(body: () -> T): Result<T> {
        delay(1.seconds)
        return Result.success(body())
    }

    override fun getTotalPlaysCount(): Flow<Int> = getDeckPlaysCount()

    override fun getUser(): Flow<User?> = preferences.getUser()
    override suspend fun updateUserRank(newRank: UserRank) {
        registerRankChange(newRank)
        preferences.updateRank(newRank)
    }

    override fun getGeneralStatistics(): Flow<GeneralStatistics> {
        return combine(
            getAnswersOf(0, false),
            getAnswersOf(0, true),
        ) { correctAns, failedAns ->
            GeneralStatistics(
                correctAnswers = correctAns,
                failedAnswers = failedAns,
            )
        }
    }

    override fun getLibraryDecksIds(): Flow<Map<Long, Boolean>> = getDownloadedDecks().map {
        it.associate { header ->
            header.id to fileManager.imagesOffline(header.id, header.cardsCount)
        }
    }

    override suspend fun deleteDeckImages(id: Long) {
        try {
            fileManager.deleteDeck(id)
            updateDeckOfflineImages(id, false)
        } catch (e: Exception) {

        }
    }


    override suspend fun setUser(name: String, age: Int) = preferences.setUser(name, age)

    override suspend fun List<DeckHeader>.applyDecksFilters(
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Map<DecksGroup, List<DeckHeader>> = when (groupBy) {
        DecksGroupType.COLLECTION -> groupBy {
            it.collection
        }.mapKeys { (collection, _) ->
            DecksGroup.Collection(collection)
        }

        DecksGroupType.LEVEL -> groupBy {
            it.level
        }.mapKeys { (level, _) ->
            DecksGroup.Level(level)
        }

        DecksGroupType.TAG -> {
            val map = mutableMapOf<String, MutableList<DeckHeader>>()
            forEach { deck ->
                deck.tags.forEach { tag ->
                    map[tag]?.add(deck) ?: run { map[tag] = mutableListOf(deck) }
                }
            }
            map.mapKeys { (tag, _) ->
                DecksGroup.Tag(tag)
            }
        }

        else -> groupBy { DecksGroup.None }
    }.mapValues { (_, value) ->
        value.applyDecksFilter(filterBy)
            .applyDecksOrder(orderBy)
            .map {
                if (fileManager.imagesOffline(it.id, it.cardsCount)) {
                    it.copy(offlineImages = true)
                } else it
            }
    }.filter { (_, value) -> value.isNotEmpty() }


    override fun getLibraryDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Flow<Map<DecksGroup, List<DeckHeader>>> =
        getDecks("%$query%").map { it.applyDecksFilters(groupBy, filterBy, orderBy) }


    override suspend fun getBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Result<List<DeckHeader>> = delayedResult {
        listOf()
    }

    private suspend fun List<DeckHeader>.filterInvalidDecks(): List<DeckHeader> = filterNot {
        it.cardsCount == 0  // no cards
    }

    override suspend fun getPaginatedBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Flow<PagingData<DeckHeader>> = flow {}

    override suspend fun getDeckCards(id: Long): Deck {
        val header = getDeck(id)!! // todo, handle not found
        val cards = getCards(id).run {
            if (header.allowShuffle) {
                shuffle()
            } else {
                sortedBy { it.index }
            }
        }
        val deck = Deck(
            header = header,
            cards = cards
        )
        return fileManager.appendFilePathForImages(deck)
    }

    override suspend fun saveDeckResults(
        id: Long,
        cardsResults: Map<Long, Boolean>,
    ) {
        val deckPlayId = insertDeckPlay(DeckPlay(deckId = id))
        val cards = cardsResults.map { (id, correct) ->
            CardPlay(
                cardId = id,
                deckPlayId = deckPlayId,
                failed = !correct,
            )
        }
        insertAllCards(cards)
    }

    override fun initializedDb() = flow {
        emit(false)
        try {
            fileManager.deleteDecks(getDownloadingDecks())
            deleteDownloadingDecks()
        } catch (e: Exception) {

        }
//        if (false) {
        if (!preferences.initializedDb()) {
            val decksCount = 5_000
            val cardsForEachDeck = 15
            val initialDeckId: Long = 100_000_000_000L
            insertDecks(
                generateLargeFakeDecks(
                    decksCount = decksCount,
                    cardsForEach = cardsForEachDeck,
                    initialDeckId = initialDeckId
                )
            )
            repeat(decksCount) {
                insertCards(
                    generateLargeFakeCardsForDeck(
                        d = it.toLong(),
                        cardsForEach = cardsForEachDeck,
                        initialDeckId = initialDeckId
                    )
                )
            }
            insertDecks(decksInitialValue)
            insertCards(cardsInitialValues.values.flatten())
//            preferences.markAsInitializedDb()
        }
        emit(true)
    }

    override fun getDatabaseInfo(): Flow<DecksDatabaseInfo> = getCollections().combine(
        getFormattedTags()
    ) { collections, tags ->
        DecksDatabaseInfo(
            tags = tags.toSet(),
            collections = collections.toSet()
        )
    }

    override suspend fun isFirstPlay(id: Long): Boolean = checkDeckFirstPlay(id)

    private fun getFormattedTags() = getTags().map {
        it.mapNotNull {
            if (it.isNotBlank()) {
                it.split(", ")
            } else {
                null
            }
        }.flatten()
    }

    override suspend fun saveDeckToLibrary(
        deck: Deck,
        downloadImages: Boolean,
    ): Flow<DownloadStatus> {
        insertDeck(deck.header.copy(downloadInProgress = true, offlineData = true))
        insertCards(deck.cards)

        return if (downloadImages) {
            downloadDeckImages(deck)
        } else {
            finishDownloadDeck(deck.header.id)
            flow {
                val status = MutableDownloadStatus {}
                status.finished = true
                status.success = true
                emit(status)
            }
        }
    }


    override suspend fun downloadDeckImages(id: Long) = downloadDeckImages(getDeckCards(id))
    override suspend fun getRankChangesStatistics(): List<UserRank> = getRanksStatistics()
    override suspend fun getPlaysStatistics(): List<DeckWithCardsPlay> = getAllPlays()
    override suspend fun getLeveledDecksCount(): List<Pair<Int, Int>> {
        val levelsCount = (min_level..max_level).associateWith { 0 }.toMutableMap()
        getDownloadedDecks().first().groupBy { it.level }.forEach {
            levelsCount[it.key] = it.value.count()
        }
        return levelsCount.toList()
    }

    override suspend fun getAllTags(): Result<List<String>> = delayedResult { listOf() }

    override suspend fun getAllCollections(): Result<List<String>> = delayedResult { listOf() }

    override fun downloadDeckImages(deck: Deck) = fileManager.saveDeck(deck).map {
        it.also {
            if (it.finished) {
                if (it.success) {
                    finishDownloadDeck(deck.header.id)
                    updateDeckOfflineImages(deck.header.id, true)
                } else {
                    deleteDownloadingDecks()
                }
            }
        }
    }

    override suspend fun deleteDeckWithCards(id: Long) {
        deleteCardsOfDeck(id)
        deleteDeck(id)
        fileManager.deleteDeck(id)
    }

    override suspend fun rateDeck(
        id: Long,
        rate: Int,
        deviceId: String,
    ): Result<DeckHeader> {
        delay(1.seconds)
        return Result.failure(NotImplementedError())
    }

    private suspend fun Result<DeckHeader>.updateDeckOnRate(): Result<DeckHeader> {
        getOrNull()?.let {
            updateDeckRate(id = it.id, rate = it.rate, rates = it.rates)
        }
        return this
    }

    override suspend fun getDeckInfo(id: Long): Result<Deck> = delayedResult {
        delay(1.seconds)
        return Result.failure(NotImplementedError())
    }
}