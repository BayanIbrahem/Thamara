package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    deckHeader: DeckHeader,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.name_x_cards_y,
                    deckHeader.name.ifBlank { stringResource(R.string.no_name) },
                    deckHeader.cardsCount
                ),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.cards_play_hint),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = buildString {
                    appendLine(stringResource(R.string.cards_play_hint_1))
                    appendLine(stringResource(R.string.cards_play_hint_2))
                    appendLine(stringResource(R.string.cards_play_hint_3))
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedButton(onClick = onStart) {
                Text(stringResource(id = R.string.play))
            }
        }
    }
}
