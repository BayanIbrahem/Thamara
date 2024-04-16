package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

enum class RankIconSize(
    val dp: Dp,
) {
    BIG(64.dp),
    MEDIUM(48.dp),
    SMALL(24.dp),
}

@Composable
fun UserRankIcon(
    modifier: Modifier = Modifier,
    size: RankIconSize = RankIconSize.MEDIUM,
    rank: Int,
    current: Boolean,
    showText: Boolean = true,
    onClickIcon: (() -> Unit)? = null,
) {
    val primary = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(size.dp)
                .drawBehind {
                    if (current) {
                        drawCircle(color = primary)
                    }
                }
                .padding(4.dp)
                .clip(CircleShape)
                .run { onClickIcon?.let { clickable(onClick = onClickIcon) } ?: this }
                .then(modifier),
            painter = rank.rankIconPainter(),
            contentDescription = null,
            tint = if (current) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
        if (showText) {
            Text(
                text = stringResource(id = R.string.rank_x, rank),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun Int.rankIconPainter(): Painter {
    return painterResource(
        id = when (this) {
            0 -> R.drawable.ic_rank_0
            1 -> R.drawable.ic_rank_1
            2 -> R.drawable.ic_rank_2
            3 -> R.drawable.ic_rank_3
            4 -> R.drawable.ic_rank_4
            5 -> R.drawable.ic_rank_5
            6 -> R.drawable.ic_rank_6
            7 -> R.drawable.ic_rank_7
            8 -> R.drawable.ic_rank_8
            9 -> R.drawable.ic_rank_9
           10 -> R.drawable.ic_rank_10
           11 -> R.drawable.ic_rank_11
           12 -> R.drawable.ic_rank_12
           13 -> R.drawable.ic_rank_13
           14 -> R.drawable.ic_rank_14
           15 -> R.drawable.ic_rank_15
           16 -> R.drawable.ic_rank_16
           17 -> R.drawable.ic_rank_17
           18 -> R.drawable.ic_rank_18
            else -> R.drawable.rank
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun UserRankIconPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            UserRankIcon(rank = 10, current = true)
        }
    }
}
