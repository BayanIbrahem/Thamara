package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.paging.PagingData
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.SimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterUiState
import com.dev_bayan_ibrahim.flashcards.ui.util.LoadableContentList
import com.dev_bayan_ibrahim.flashcards.ui.util.LoadableContentMap
import com.dev_bayan_ibrahim.flashcards.ui.util.MutableLoadableContentList
import com.dev_bayan_ibrahim.flashcards.ui.util.MutableLoadableContentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Stable
interface DecksUiState {
    val query: String

    val filterDialogState: DecksFilterUiState

    val libraryDecks: Map<DecksGroup, List<DeckHeader>>
    val searchResults: LoadableContentMap<DecksGroup, List<DeckHeader>>
    val paginatedSearchResults: StateFlow<PagingData<DeckHeader>>

    val selectedDeck: DeckHeader?

    val downloadStatus: SimpleDownloadStatus

    val allTags: LoadableContentList<String>
    val allCollections: LoadableContentList<String>
}

class DecksMutableUiState : DecksUiState {
    override var query: String by mutableStateOf("")

    override val filterDialogState: DecksFilterMutableUiState = DecksFilterMutableUiState()

    override val libraryDecks: SnapshotStateMap<DecksGroup, List<DeckHeader>> = mutableStateMapOf()
    override val searchResults: MutableLoadableContentMap<DecksGroup, List<DeckHeader>> = MutableLoadableContentMap()
    override val paginatedSearchResults: MutableStateFlow<PagingData<DeckHeader>> = MutableStateFlow(PagingData.empty())

    override var selectedDeck: DeckHeader? by mutableStateOf(null)

    override var downloadStatus: SimpleDownloadStatus by mutableStateOf(SimpleDownloadStatus.NOT_DOWNLOADED)

    override val allTags: MutableLoadableContentList<String> = MutableLoadableContentList()
    override val allCollections: MutableLoadableContentList<String> = MutableLoadableContentList()
}
