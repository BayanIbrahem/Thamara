package com.dev_bayan_ibrahim.flashcards.ui.app.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.rankIconPainter


@Composable
fun UserRankChangeDialog(
    modifier: Modifier = Modifier,
    rank: UserRank
) {
    var lastVisibleRank by rememberSaveable {
        mutableIntStateOf(rank.rank)
    }
    var show by remember {
        mutableStateOf(false)
    }
    var rise by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = rank) {
        if (rank.rank != lastVisibleRank) {
            rise = rank.rank - lastVisibleRank > 0
            lastVisibleRank = rank.rank
            show = true
        }
    }
    FlashDialog(
        modifier = modifier
            .height(300.dp)
            .aspectRatio(cardRatio),
        show = show,
        accent = if (rise) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        },
        onDismiss = { show = false }
    ) {
        val color = if (rise) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if (rise) stringResource(R.string.new_rank) else stringResource(R.string.rank_drop),
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )
            Icon(
                modifier = Modifier.size(64.dp),
                painter = rank.rank.rankIconPainter(),
                contentDescription = null,
                tint = color,
            )
            Text(
                text = stringResource(R.string.rank_x, rank.asString(false)),
                style = MaterialTheme.typography.titleSmall,
                color = color,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (rise) stringResource(R.string.promotion_congrats) else stringResource(R.string.rank_drop_message),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = color,
            )
            TextButton(
                onClick = { show = false },
                colors = ButtonDefaults.textButtonColors(contentColor = color)
            ) {
                Text(text = stringResource(R.string.close))
            }
        }
    }
}