package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.SimpleDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun LibraryDeckDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    deck: DeckHeader,
    downloadStatus: SimpleDownloadStatus,
    onDeleteDeck: (id: Long) -> Unit,
    onRemoveDeckImages: (id: Long) -> Unit,
    onDownloadDeckImages: (id: Long) -> Unit,
    onDismiss: () -> Unit,
    onPlay: () -> Unit,
) {
    DecksListDeckDialog(
        modifier = modifier
            .height(400.dp)
            .aspectRatio(cardRatio),
        show = show,
        deck = deck,
        isLibrary = true,
        downloadStatus = downloadStatus,
        onDismiss = onDismiss,
        onDeleteDeck = onDeleteDeck,
        onRemoveDeckImages = onRemoveDeckImages,
        onDownloadDeckImages = onDownloadDeckImages,
    ) {
        Button(onClick = onPlay, enabled = downloadStatus != SimpleDownloadStatus.LOADING) {
            Text(stringResource(id = R.string.play))
        }
    }
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
