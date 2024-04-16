package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerp
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

const val min_level: Int = 0
const val max_level: Int = 10

enum class LevelIconSize(val dp: Dp) {
    BIG(48.dp),
    MEDIUM(32.dp),
    SMALL(24.dp),
}

@Composable
fun DeckLevelIcon(
    modifier: Modifier = Modifier,
    size: LevelIconSize = LevelIconSize.MEDIUM,
    level: Int,
    current: Boolean,
    tint: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    lerpDifficultyLevelColor: Boolean = true,
    onClickIcon: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(size.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .run { onClickIcon?.let { clickable(onClick = onClickIcon) } ?: this }
                .then(modifier),
            painter = level.levelIconPainter(current),
            contentDescription = stringResource(R.string.level_x, level),
            tint = if (lerpDifficultyLevelColor) {
                tint.lerp(level.getLevelDifficultyColor(), 0.5f)

            } else {
                tint
            }
        )
    }
}


@Composable
fun Int.levelIconPainter(current: Boolean): Painter {
    return painterResource(
        id = when (this) {
            0 -> if (current) R.drawable.ic_level_fill_1 else R.drawable.ic_level_outline_1
            1 -> if (current) R.drawable.ic_level_fill_2 else R.drawable.ic_level_outline_2
            2 -> if (current) R.drawable.ic_level_fill_3 else R.drawable.ic_level_outline_3
            3 -> if (current) R.drawable.ic_level_fill_4 else R.drawable.ic_level_outline_4
            4 -> if (current) R.drawable.ic_level_fill_5 else R.drawable.ic_level_outline_5
            5 -> if (current) R.drawable.ic_level_fill_6 else R.drawable.ic_level_outline_6
            6 -> if (current) R.drawable.ic_level_fill_7 else R.drawable.ic_level_outline_7
            7 -> if (current) R.drawable.ic_level_fill_8 else R.drawable.ic_level_outline_8
            8 -> if (current) R.drawable.ic_level_fill_9 else R.drawable.ic_level_outline_9
            9 -> if (current) R.drawable.ic_level_fill_10 else R.drawable.ic_level_outline_10
            else -> if (current) R.drawable.ic_level_fill_11 else R.drawable.ic_level_outline_11
        }
    )
}

private fun Int.getLevelDifficultyColor(): Color {
    val levelRange = max_level - min_level
    val percent = ((toFloat() - min_level) / levelRange).coerceIn(0f..1f)
    return if (percent < 0.5f) {
        Color.Yellow.lerp(Color.Green, percent * 2)
    } else {
        Color.Red.lerp(Color.Yellow, percent * 2f - 1f)
    }
}

@Preview(showBackground = true)
@Composable
private fun UserRankIconPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(11) {
                    DeckLevelIcon(
                        level = it.inc(),
                        current = false
                    )
                }
            }
        }
    }
}
