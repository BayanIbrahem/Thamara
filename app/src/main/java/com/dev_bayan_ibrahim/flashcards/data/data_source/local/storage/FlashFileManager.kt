package com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import kotlinx.coroutines.flow.Flow


interface FlashFileManager {
    fun saveDeck(
        deck: Deck,
    ): Flow<DownloadStatus>

    suspend fun appendFilePathForImages(deck: Deck): Deck
    suspend fun deleteDeck(deck: Deck) = deleteDeck(deck.header.id)
    suspend fun deleteDeck(id: Long)
    suspend fun deleteDecks(ids: List<Long>)
    fun imagesOffline(id: Long, cardsCount: Int): Boolean
}