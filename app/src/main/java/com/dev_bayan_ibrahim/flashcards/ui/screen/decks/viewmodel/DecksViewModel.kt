package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result.asSuccessResult
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckNotFoundException
import com.dev_bayan_ibrahim.flashcards.data.exception.mapUnknownErrorIfOffline
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.SimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.asSimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarMessages
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.app.util.network.NetworkMonitor
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toIntRange
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.BROWSE
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.LIBRARY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val repo: FlashRepo,
    private val filesDispatcher: CoroutineContext,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {
    val state = DecksMutableUiState()

    var appliedFilters = DecksFilterMutableUiState()
    var selectedTab = LIBRARY

    val dbInfo = repo.getDatabaseInfo().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DecksDatabaseInfo()
    )
    val libraryDecksIds = repo.getLibraryDecksIds().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mapOf()
    )

    private var downloadStatus: DownloadStatus? by mutableStateOf(null)


    private val lastAppliedFiltersKey = mutableMapOf<DecksTab, Int>()
    fun getDecksActions(
        navigateToDeckPlay: (Long) -> Unit,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
    ): DecksUiActions = object : DecksUiActions {
        override fun onSearchQueryChange(query: String) {
            state.query = query
            getLibraryDecks()
        }

        override fun onClickDeck(deck: DeckHeader) {
            state.selectedDeck = deck
            state.downloadStatus =
                (libraryDecksIds.value[deck.id] ?: false).asSimpleDownloadStatus()
        }

        override fun onSearch() {
            onApplyFilters(onShowSnackbarMessage = onShowSnackbarMessage)
        }

        override fun onSelectTab(tab: DecksTab) {
            selectedTab = tab
            onApplyFilters(onShowSnackbarMessage = onShowSnackbarMessage)
        }

        override fun onDownloadDeck(downloadImages: Boolean) {
            if (selectedTab == BROWSE) {
                state.selectedDeck?.let {
                    saveAndDownloadDeck(
                        header = it,
                        onShowSnackbarMessage = onShowSnackbarMessage,
                        isLocal = false,
                        downloadImages = downloadImages,
                    ) { error ->
                        if (error == null) {
                            onShowSnackbarMessage(FlashSnackbarMessages.FinishSaveDeck)
                        } else {
                            onShowSnackbarMessage(FlashSnackbarMessages.Factory(error))
                        }
                    }
                }
            }
        }

        override fun onCancelDownloadDeck() {
            if (selectedTab == BROWSE) {
                state.selectedDeck?.let {
                    cancelDownloadDeck(it)
                    state.selectedDeck = null
                }
            }
        }

        override fun onPlayDeck() {
            state.selectedDeck?.let {
                navigateToDeckPlay(it.id)
                state.selectedDeck = null
            }
        }

        override fun onDismissSelectedDeck() {
            state.selectedDeck = null
        }

        override fun onDeleteDeck(id: Long) {
            if (id in libraryDecksIds.value) { // in library
                viewModelScope.launch(IO) {
                    try {
                        repo.deleteDeckWithCards(id)
                    } catch (e: Exception) {
                        onShowSnackbarMessage(FlashSnackbarMessages.Factory(e))
                    }
                    state.selectedDeck = null
                }
            }
        }

        override fun onRemoveDeckImages(id: Long) {
            if (id in libraryDecksIds.value) {
                viewModelScope.launch {
                    repo.deleteDeckImages(id)
                    state.selectedDeck = null
                }
            }
        }

        override fun onDownloadDeckImages(id: Long) {
            if (id in libraryDecksIds.value) {
                state.selectedDeck?.let {
                    saveAndDownloadDeck(
                        header = it,
                        onShowSnackbarMessage = onShowSnackbarMessage,
                        isLocal = true,
                        downloadImages = true,
                    ) { error ->
                        if (error == null) {
                            onShowSnackbarMessage(
                                FlashSnackbarMessages.FinishSaveDeck
                            )
                        } else {
                            onShowSnackbarMessage(FlashSnackbarMessages.Factory(error))
                        }
                    }
                }
            }
        }

        override fun onRefresh() {
            getBrowseDecks(onShowSnackbarMessage = onShowSnackbarMessage)
        }

        override fun onSelectGroupType(groupType: DecksGroupType) {
            state.filterDialogState.groupType = groupType
        }

        override fun onSelectOrderType(orderType: DecksOrderType) {
            state.filterDialogState.orderType = orderType
        }

        override fun onSelectTag(tag: String) {
            state.filterDialogState.run {
                val tags = if (tag in (filter.tags)) {
                    filter.tags.remove(tag)
                } else {
                    filter.tags.add(tag)
                }
                filter = filter.copy(tags = tags)
            }
        }

        override fun onDeselectAllTags() {
            state.filterDialogState.run {
                filter = filter.copy(tags = persistentSetOf())
            }
        }

        override fun onSelectCollection(collection: String) {
            state.filterDialogState.run {
                val collections = if (collection in (filter.collections)) {
                    filter.collections.remove(collection)
                } else {
                    filter.collections.add(collection)
                }
                val newFilter = filter.copy(collections = collections)
                filter = newFilter
            }
        }

        override fun onDeselectAllCollections() {
            state.filterDialogState.run {
                filter = filter.copy(collections = persistentSetOf())
            }
        }

        override fun onShowDialog() {
            state.run {
                filterDialogState.show = true
                viewModelScope.launch {
                    if (allCollections.needInitialize()) {
                        allCollections.loading = true
                        allCollections.error = null
                        repo.getAllCollections().mapUnknownErrorIfOffline(networkMonitor.isOnline)
                            .fold(
                                onSuccess = {
                                    allCollections.content.run {
                                        clear()
                                        addAll(it)
                                    }
                                    allCollections.initialized = true
                                },
                                onFailure = {
                                    allCollections.error = it
                                    onShowSnackbarMessage(FlashSnackbarMessages.Factory(it))
                                },
                            )
                        allCollections.loading = false
                    }
                }
                viewModelScope.launch {
                    if (allTags.needInitialize()) {
                        allTags.loading = true
                        allTags.error = null
                        repo.getAllTags().mapUnknownErrorIfOffline(networkMonitor.isOnline).fold(
                            onSuccess = {
                                allTags.content.run {
                                    clear()
                                    addAll(it)
                                }
                                allTags.initialized = true
                            },
                            onFailure = {
                                allTags.error = it
                                onShowSnackbarMessage(FlashSnackbarMessages.Factory(it))
                            },
                        )
                        allTags.loading = false
                    }
                }
            }
        }

        override fun changeOrderType(asc: Boolean) {
            state.filterDialogState.ascOrder = asc
        }

        override fun onApply() {
            state.filterDialogState.show = false
            appliedFilters = state.filterDialogState
            onApplyFilters(onShowSnackbarMessage = onShowSnackbarMessage)
        }

        override fun onCancel() {
            state.filterDialogState.show = false
            state.filterDialogState.onApply(appliedFilters)
        }

        override fun onReset() {
            state.filterDialogState.reset()
        }

        override fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>) {
            state.filterDialogState.run {
                filter = filter.copy(levels = levels.toIntRange())
            }
        }

        override fun onRateValueChange(rates: ClosedFloatingPointRange<Float>) {
            state.filterDialogState.run {
                filter = filter.copy(rate = rates)
            }
        }
    }

    private var initialized = false
    fun initScreen() {
        viewModelScope.launch {
            if (!initialized) {
                getLibraryDecks()
                initialized = true
            }
        }
    }

    private fun onApplyFilters(onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit) {
        val lastKey = lastAppliedFiltersKey[selectedTab]
        val currentKey = appliedFilters.getValuesKey(state.query)
        if (lastKey != currentKey) {
            lastAppliedFiltersKey[selectedTab] = currentKey
            when (selectedTab) {
                LIBRARY -> getLibraryDecks()
                BROWSE -> getBrowseDecks(onShowSnackbarMessage = onShowSnackbarMessage)
            }
        }
    }

    private fun getLibraryDecks(
        query: String = state.query,
        groupBy: DecksGroupType? = appliedFilters.groupType,
        filterBy: DecksFilter? = appliedFilters.filter,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
    ) {
        viewModelScope.launch(filesDispatcher) {
            repo.getLibraryDecks(
                query = query,
                groupBy = groupBy,
                filterBy = filterBy,
                orderBy = orderBy?.toDeckOrder(ascOrder)
            ).distinctUntilChanged()
                .collect { decks ->
                    state.libraryDecks.apply {
                        clear()
                        putAll(decks)
                    }
                }
        }
    }

    private var cachedAllDecks: List<DeckHeader>? = null

    private fun getBrowseDecks(
        query: String = state.query,
        groupBy: DecksGroupType? = appliedFilters.groupType,
        filterBy: DecksFilter? = appliedFilters.filter,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
    ) {
        viewModelScope.launch {

            state.searchResults.run {
                loading = true
                error = null
                (cachedAllDecks?.asSuccessResult() ?: repo.getBrowseDecks(
                    query = query,
                    groupBy = groupBy,
                    filterBy = filterBy,
                    orderBy = orderBy?.toDeckOrder(ascOrder)
                )).mapUnknownErrorIfOffline(networkMonitor.isOnline).fold(
                    onSuccess = {
                        cachedAllDecks = it
                        content.run {
                            clear()
                            with(repo) {
                                putAll(
                                    it.filter { query.isBlank() || it.name.contains(query.trim()) }.applyDecksFilters(
                                        groupBy = groupBy,
                                        filterBy = filterBy,
                                        orderBy = orderBy?.toDeckOrder(ascOrder)
                                    )
                                )
                            }
                        }
                        loading = false
                        initialized = true
                    },
                    onFailure = {
                        loading = false
                        error = it
                    },
                )
            }

//            repo.getPaginatedBrowseDecks(
//                query = query,
//                groupBy = groupBy,
//                filterBy = filterBy,
//                orderBy = orderBy?.toDeckOrder(ascOrder)
//            ).distinctUntilChanged()
//                .cachedIn(viewModelScope)
//                .collect { pagingData ->
//                    state.searchResults.value = pagingData
//                }
        }
    }

    private fun saveAndDownloadDeck(
        header: DeckHeader,
        downloadImages: Boolean,
        isLocal: Boolean,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
        onFinish: (Throwable?) -> Unit = {}
    ) {
        viewModelScope.launch(IO) {
            state.downloadStatus = SimpleDownloadStatus.LOADING

            val deck = if (isLocal) repo.getDeckCards(header.id) else getDeckInfo(
                header.id,
                onShowSnackbarMessage
            )
            deck?.let { deck ->
                repo.saveDeckToLibrary(deck, downloadImages).collect {
                    downloadStatus = it
                    if (it.finished) {
                        state.downloadStatus = it.success.asSimpleDownloadStatus()
                    }
                }
            } ?: let {
                onFinish(DeckNotFoundException(id = header.id, searchLocal = false))
                downloadStatus = null
            }

            onFinish(downloadStatus?.error)
            downloadStatus = null
        }
    }

    private suspend fun getDeckInfo(
        id: Long,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
    ): Deck? {
        return repo.getDeckInfo(id).mapUnknownErrorIfOffline(networkMonitor.isOnline).fold(
            onSuccess = {
                it
            },
            onFailure = { error ->
                onShowSnackbarMessage(FlashSnackbarMessages.Factory(error))
                null
            }
        )
    }

    private fun cancelDownloadDeck(deck: DeckHeader) {
        downloadStatus?.cancelDownload?.let { cancel ->
            viewModelScope.launch {
                cancel()
                repo.deleteDeckImages(deck.id)
            }
        }
        downloadStatus = null
    }
}