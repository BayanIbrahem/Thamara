package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.SimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.toFormattedSize
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun DownloadDeckDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    deck: DeckHeader,
    downloadStatus: SimpleDownloadStatus,
    onDownload: (downloadImages: Boolean) -> Unit,
    onCancel: () -> Unit,
) {
    DecksListDeckDialog(
        modifier = modifier,
        show = show,
        deck = deck,
        isLibrary = false,
        downloadStatus = downloadStatus,
        onDismiss = { if (downloadStatus != SimpleDownloadStatus.LOADING) onCancel() },
        onDeleteDeck = {},
        onRemoveDeckImages = {},
        onDownloadDeckImages = {},
    ) {
        DownloadActions(
            downloadStatus = downloadStatus,
            onCancel = onCancel,
            onDownload = onDownload
        )
    }
}

@Composable
private fun DownloadActions(
    modifier: Modifier = Modifier,
    downloadStatus: SimpleDownloadStatus,
    onCancel: () -> Unit,
    onDownload: (downloadImages: Boolean) -> Unit,
) {
    if (downloadStatus == SimpleDownloadStatus.LOADING) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                )
                IconButton(
                    onClick = onCancel
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FilledIconButton(
                modifier = modifier,
                onClick = { onDownload(true) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download_with_image),
                    contentDescription = stringResource(R.string.download_with_images)
                )
            }
            FilledIconButton(
                modifier = modifier,
                onClick = { onDownload(false) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download_wihtout_image),
                    contentDescription = stringResource(R.string.download_without_images)
                )
            }
        }
    }
}

@Composable
private fun Long?.formatSize(): String = (this ?: 0).toFormattedSize().getValue()

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
