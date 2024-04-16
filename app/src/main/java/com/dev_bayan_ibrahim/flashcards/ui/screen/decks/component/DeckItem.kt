package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerpOnSurface
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.DeckCard
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.DeckLevelIcon
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme


@Composable
fun DeckItem(
    modifier: Modifier = Modifier,
    deckHeader: DeckHeader,
    overrideOfflineData: Boolean? = null,
    overrideOfflineImages: Boolean? = null,
    onClick: (() -> Unit)?,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier.padding(4.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            DeckCard(
                modifier = Modifier.aspectRatio(cardRatio),
                accent = deckHeader.colorAccent,
                onClick = onClick ?: {},
                enableClick = onClick != null,
            ) {
                DeckLevelIcon(
                    level = deckHeader.level,
                    current = false,
                    tint = deckHeader.colorAccent.lerpOnSurface(),
                    lerpDifficultyLevelColor = false
                )
            }
            DownloadIcon(
                offlineData = overrideOfflineData ?: deckHeader.offlineData,
                offlineImage = overrideOfflineImages ?: deckHeader.offlineImages,
                tint = deckHeader.colorAccent.lerpOnSurface(),
            )
        }
        Text(
            modifier = Modifier.widthIn(max = smallCardWidth),
            text = deckHeader.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoxScope.DownloadIcon(
    offlineData: Boolean,
    offlineImage: Boolean,
    tint: Color,
) {
    val iconWithDescription by remember {
        derivedStateOf {
            if (offlineImage) {
                R.drawable.ic_offline_image to R.string.offline_data_with_images
            } else if (offlineData) {
                R.drawable.ic_no_images to R.string.offline_data_without_images
            } else null
        }
    }

    iconWithDescription?.let { (icon, description) ->
        Icon(
            modifier = Modifier
                .padding(2.dp)
                .size(16.dp)
                .align(Alignment.BottomEnd),
            painter = painterResource(icon),
            contentDescription = stringResource(id = description),
            tint = tint,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
private fun DeckItemPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val deck = DeckHeader(
                color = Color.Red.toArgb(),
                cardsCount = 10,
                id = 0
            )
            val colors = listOf(
                Color(0xFF000000),
                Color(0xFF444444),
                Color(0xFF888888),
                Color(0xFFCCCCCC),
                Color(0xFFFFFFFF),
                Color(0xFFFF0000),
                Color(0xFF00FF00),
                Color(0xFF0000FF),
                Color(0xFFFFFF00),
                Color(0xFF00FFFF),
                Color(0xFFFF00FF),
            )
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                colors.forEach {
                    DeckItem(
                        deckHeader = DeckHeader(
                            name = "deck $it",
                            color = it.toArgb(),
                            cardsCount = 10,
                            id = 0,
                            offlineData = true,
                            offlineImages = true
                        )
                    ) { }
                }
            }
        }
    }
}
