@file:OptIn(ExperimentalMaterial3Api::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.SimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.asFormattedString
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.DeckLevelIcon
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashLazyRowTooltip
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.LevelIconSize
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.RateStars
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.RateStarsSize
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.max_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.min_level
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.dev_bayan_ibrahim.flashcards.ui.util.asFlashPlural
import kotlinx.coroutines.launch

@Composable
fun DecksListDeckDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    deck: DeckHeader,
    isLibrary: Boolean,
    downloadStatus: SimpleDownloadStatus,
    onDismiss: () -> Unit,
    onDeleteDeck: (id: Long) -> Unit,
    onRemoveDeckImages: (id: Long) -> Unit,
    onDownloadDeckImages: (id: Long) -> Unit,
    content: @Composable () -> Unit
) {
    FlashDialog(
        modifier = modifier
            .height(400.dp)
            .aspectRatio(cardRatio),
        show = show,
        onDismiss = onDismiss,
    ) {
        HeaderActions(
            isLibrary = isLibrary,
            onDismiss = onDismiss,
            downloadStatus = downloadStatus,
            onDelete = { onDeleteDeck(deck.id) },
            onRemoveImages = { onRemoveDeckImages(deck.id) },
            onDownloadImages = { onDownloadDeckImages(deck.id) },
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeckItem(
                modifier = Modifier
                    .height(100.dp)
                    .aspectRatio(cardRatio),
                deckHeader = deck,
                onClick = null
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = deck.name,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
                deck.collection?.let { collection ->
                    Text(
                        text = collection,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            InfoItems(
                rate = deck.rate,
                rates = deck.rates,
                level = deck.level,
                cardsCount = deck.cardsCount,
                tags = deck.tags
            )
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InfoItems(
    modifier: Modifier = Modifier,
    rate: Float,
    rates: Int,
    level: Int,
    cardsCount: Int,
    tags: List<String>,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(
                id = R.string.rate_x,
                buildString {
                    append("(")
                    append(rates.toLong().asFormattedString())
                    append(" ")
                    append(stringResource(R.string.rates))
                    append(")")
                }
            ),
        ) {
            RateStars(
                size = RateStarsSize.MEDIUM,
                rate = rate,
                rates = rates,
                outline = MaterialTheme.colorScheme.onPrimaryContainer,
                fill = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.level),
//            value = level.toString()
        ) {
            val state = rememberTooltipState(isPersistent = true)
            val scope = rememberCoroutineScope()
            FlashLazyRowTooltip(
                state = state,
                itemWidth = LevelIconSize.MEDIUM.dp,
                visibleItemsCount = 5,
                currentItemIndex = level,
                items = {
                    items((min_level..max_level).toList()) {
                        DeckLevelIcon(
                            size = LevelIconSize.BIG,
                            level = it,
                            current = it == level,
                            lerpDifficultyLevelColor = it == level,

                            )
                    }
                }
            ) {
                DeckLevelIcon(
                    size = LevelIconSize.MEDIUM,
                    level = level,
                    current = false,
                    lerpDifficultyLevelColor = false,
                    onClickIcon = {
                        scope.launch {
                            state.show()
                        }
                    }
                )
            }
        }
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.cards),
            value = cardsCount.asFlashPlural(id = R.plurals.card)
        )
        if (tags.isNotEmpty()) {
            InfoItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(Int.MAX_VALUE),
                label = stringResource(R.string.tags),
                value = tags.joinToString(" - ")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
//        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
//        Spacer(modifier = Modifier.weight(1f))
        content()
    }
}

@Composable
private fun BoxScope.HeaderActions(
    modifier: Modifier = Modifier,
    isLibrary: Boolean,
    downloadStatus: SimpleDownloadStatus,
    onDismiss: () -> Unit,
    onDelete: () -> Unit = {},
    onRemoveImages: () -> Unit = {},
    onDownloadImages: () -> Unit = {},
) {
    var deleteImagesOnly: Boolean? by remember {
        mutableStateOf(null)
    }
    val deleteAction by remember(deleteImagesOnly) {
        derivedStateOf {
            deleteImagesOnly?.let {
                if (it) onRemoveImages else onDelete
            } ?: {}
        }
    }

    deleteImagesOnly?.let { imagesOnly ->
        DeleteConfirmDialog(
            message = if (imagesOnly) {
                stringResource(R.string.remove_deck_images_hint)
            } else {
                stringResource(R.string.remove_deck_permanently_hint)
            },
            onConfirm = deleteAction,
            onCancel = { deleteImagesOnly = null }
        )
    }
    Row(
        modifier = modifier.align(Alignment.TopCenter),
    ) {
        IconButton(
            onClick = onDismiss,
            enabled = downloadStatus != SimpleDownloadStatus.LOADING
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.close),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isLibrary) {
            when (downloadStatus) {
                SimpleDownloadStatus.DOWNLOADED -> {
                    IconButton(
                        onClick = { deleteImagesOnly = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete_images),
                            contentDescription = stringResource(id = R.string.close),
                        )
                    }
                }

                SimpleDownloadStatus.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }

                SimpleDownloadStatus.NOT_DOWNLOADED -> {
                    IconButton(
                        onClick = onDownloadImages
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download_with_image),
                            contentDescription = stringResource(id = R.string.close),
                        )
                    }
                }
            }
            IconButton(
                onClick = { deleteImagesOnly = false },
                enabled = downloadStatus != SimpleDownloadStatus.LOADING
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_forever),
                    contentDescription = stringResource(R.string.delete_deck),
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmDialog(
    modifier: Modifier = Modifier,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = stringResource(R.string.confirm_delete))
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
private fun DownloadDeckDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val downloadStatus = SimpleDownloadStatus.DOWNLOADED
            DownloadDeckDialog(
                deck = DeckHeader(
                    name = "name",
                    level = 5,
                    rate = 1.4f,
                    rates = 100,
                    cardsCount = 5,
                    id = 0,
                ),
                downloadStatus = downloadStatus,
                onCancel = {},
                onDownload = {},
                show = true
            )
        }
    }
}