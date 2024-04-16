package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.ui.app.util.getThrowableMessage
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth
import com.dev_bayan_ibrahim.flashcards.ui.util.LoadableContentMap
import com.dev_bayan_ibrahim.flashcards.ui.util.asFlashPlural
import java.util.Objects

@Composable
fun PaginatedDecksList(
    modifier: Modifier = Modifier,
    decks: LazyPagingItems<DeckHeader>,
    libraryDecksIds: Map<Long, Boolean>,
    onClickDeck: (DeckHeader) -> Unit,
) {
    DecksGrid(modifier = modifier) {
        countItem(decks.itemCount)
        lazyPagingLoadState(loadState = decks.loadState.prepend, retry = decks::retry)
        lazyPagingLoadState(
            loadState = decks.loadState.refresh,
            retry = decks::retry
        ) {
            repeat(decks.itemCount) {
                decks[it]?.let { deck ->
                    val offlineImages = libraryDecksIds[deck.id]

                    deckItem(
                        deck = deck,
                        overrideOfflineData = offlineImages?.let { true },
                        overrideOfflineImages = offlineImages,
                        onClickDeck = onClickDeck
                    )
                }
            }
        }
        lazyPagingLoadState(loadState = decks.loadState.append, retry = decks::retry)
    }
}

fun LazyGridScope.lazyPagingLoadState(
    loadState: LoadState,
    retry: () -> Unit,
    notLoadingItems: (LazyGridScope.() -> Unit)? = null,
) {
    when (loadState) {
        is LoadState.Error -> item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = getThrowableMessage(throwable = loadState.error))
                OutlinedButton(onClick = retry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }

        LoadState.Loading -> item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        }

        is LoadState.NotLoading -> notLoadingItems?.let { items -> items() }
    }
}



@Composable
fun LoadableDecksList(
    modifier: Modifier = Modifier,
    decksGroups: LoadableContentMap<DecksGroup, List<DeckHeader>>,
    libraryDecksIds: Map<Long, Boolean>,
    onClickDeck: (DeckHeader) -> Unit,
    onRefresh: () -> Unit,
) {
    val totalCount by remember(decksGroups.content) {
        derivedStateOf {
            decksGroups.content.map { (_, values) ->
                values.map { it.id }
            }.flatten().toSet().count()
        }
    }
    DecksGrid(
        modifier = modifier,
    ) {
        countItem(totalCount)
        if (decksGroups.loading) {
            lazyPagingLoadState(LoadState.Loading, retry = {})
        } else if (decksGroups.error != null) {
            decksGroups.error?.let { error ->
                lazyPagingLoadState(LoadState.Error(error), retry = onRefresh)
            }
        } else {
            decksGroups.content.forEach { (group, decks) ->
                groupTitle(group)
                deckItems(
                    decks = decks,
                    libraryDecksIds = libraryDecksIds,
                    groupId = group.name,
                    onClickDeck = onClickDeck
                )
            }
        }
    }
}
@Composable
fun DecksList(
    modifier: Modifier = Modifier,
    decksGroups: Map<DecksGroup, List<DeckHeader>>,
    onClickDeck: (DeckHeader) -> Unit,
) {
    val totalCount by remember(decksGroups) {
        derivedStateOf {
            decksGroups.map { (_, values) ->
                values.map { it.id }
            }.flatten().toSet().count()
        }
    }
    DecksGrid(
        modifier = modifier,
    ) {
        countItem(totalCount)
        decksGroups.forEach { (group, decks) ->
            groupTitle(group)
            deckItems(
                decks = decks,
                groupId = group.name,
                libraryDecksIds = null,
                onClickDeck = onClickDeck
            )
        }
    }
}

@Composable
private fun DecksGrid(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(smallCardWidth),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        content = content,
        contentPadding = PaddingValues(8.dp)
    )
}

private fun LazyGridScope.deckItem(
    deck: DeckHeader,
    overrideOfflineData: Boolean? = null,
    overrideOfflineImages: Boolean? = null,
    onClickDeck: (DeckHeader) -> Unit,
) {
    item(
        key = deck.id,
        contentType = { "c" }
    ) {
        DeckItem(
            deckHeader = deck,
            overrideOfflineData = overrideOfflineData,
            overrideOfflineImages = overrideOfflineImages,
        ) { onClickDeck(deck) }
    }
}

private fun LazyGridScope.deckItems(
    decks: List<DeckHeader>,
    groupId: String? = null,
    libraryDecksIds: Map<Long, Boolean>?,
    onClickDeck: (DeckHeader) -> Unit,
) {
    items(
        items = decks,
        key = { Objects.hash(it.id, groupId) },
        contentType = { "c" }
    ) { deck ->
        DeckItem(
            deckHeader = deck,
            overrideOfflineData = libraryDecksIds?.containsKey(deck.id) ?: false,
            overrideOfflineImages = libraryDecksIds?.get(deck.id),
        ) { onClickDeck(deck) }
    }
}

private fun LazyGridScope.countItem(
    count: Int
) {
    item(
        contentType = "c",
        span = {
            GridItemSpan(maxLineSpan)
        },
    ) {
        Text(
            text = count.asFlashPlural(id = R.plurals.deck),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

private fun LazyGridScope.groupTitle(
    group: DecksGroup,
) {
    if (group.name.isNotBlank()) {
        item(
            key = group.name,
            contentType = "b",
            span = {
                GridItemSpan(maxLineSpan)
            },
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}