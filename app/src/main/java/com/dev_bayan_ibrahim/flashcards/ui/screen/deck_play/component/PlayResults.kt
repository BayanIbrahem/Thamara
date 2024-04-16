package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashSlider
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.IncorrectExpandedCard
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val resultAnim = 500
private const val resultAnimDelay = 1000

@Composable
fun PlayResults(
    modifier: Modifier = Modifier,
    count: Int,
    correctAnswers: Int,
    incorrectCards: List<Pair<Card, String?>>, // card, answer
    accent: Color,
    bgPattern: String?,
    isRateLoading: Boolean,
    onRepeat: () -> Unit,
    onClose: () -> Unit,
    onRate: (rate: Int) -> Unit,
) {
    var animatableValue by remember {
        mutableStateOf(0f)
    }

    val animatable by remember {
        mutableStateOf(Animatable(animatableValue))
    }
    LaunchedEffect(key1 = Unit) {
        val targetValue = correctAnswers.toFloat() / count * 100
        animatableValue = targetValue
        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = tween(resultAnim, resultAnimDelay)
        )
    }
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if(isRateLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).align(Alignment.TopEnd),
                )
            } else {
                var showRateDialog by remember {
                    mutableStateOf(false)
                }

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { showRateDialog = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rate),
                        contentDescription = stringResource(id = R.string.rate)
                    )
                    RateDialog(
                        show = showRateDialog,
                        onDismiss = { showRateDialog = false },
                        onRate = onRate
                    )
                }
            }

            Text(
                text = "${animatable.value.roundToInt()}%",
                style = MaterialTheme.typography.displayMedium
            )
        }
        FlashSlider(
            value = animatable.value,
            steps = 0,
            onValueChange = {},
            onValueChangeFinished = { },
            valueRange = 0f..100f
        )
        Text(
            text = stringResource(R.string.card_answers_result, correctAnswers, count),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.incorrect_answers_x, count - correctAnswers),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(onClick = onClose) {
                Text(text = stringResource(id = R.string.close))
            }
            OutlinedButton(onClick = onRepeat) {
                Text(text = stringResource(R.string.repeat))
            }
        }
        IncorrectCardsPager(
            cards = incorrectCards.mapNotNull { (key, value) ->
                value?.let { key to value }
            },
            accent = accent,
            bgPattern = bgPattern
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IncorrectCardsPager(
    modifier: Modifier = Modifier,
    cards: List<Pair<Card, String>>, // card, answer
    accent: Color,
    bgPattern: String?,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { cards.count() }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        IconButton(
            enabled = pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage.dec().coerceAtLeast(0)
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.previous)
            )
        }
        HorizontalPager(
            modifier = Modifier.weight(1f),
//                .aspectRatio(cardRatio),
            state = pagerState,
            userScrollEnabled = true,
        ) { i ->
            val (card, answer) = cards[i]
            Box(
                modifier = Modifier,
            ) {
                IncorrectExpandedCard(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    card = card,
                    accent = accent,
                    bgPattern = bgPattern,
                    incorrectAnswer = answer
                )
            }
        }

        IconButton(
            enabled = pagerState.currentPage < cards.count().dec(),
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage.inc().coerceAtMost(cards.count().dec())
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.next)
            )
        }
    }
}

@Composable
private fun RateDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismiss: () -> Unit,
    onRate: (rate: Int) -> Unit,
) {
    var selected: Int? by remember {
        mutableStateOf(null)
    }
    if (show) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(id = R.string.rate))
            },
            text = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { selected = index.inc() }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = when (index) {
                                        0 -> if (selected == index.inc()) R.drawable.ic_level_fill_10 else R.drawable.ic_level_outline_10
                                        1 -> if (selected == index.inc()) R.drawable.ic_level_fill_8 else R.drawable.ic_level_outline_8
                                        2 -> if (selected == index.inc()) R.drawable.ic_level_fill_5 else R.drawable.ic_level_outline_5
                                        3 -> if (selected == index.inc()) R.drawable.ic_level_fill_3 else R.drawable.ic_level_outline_3
                                        else -> if (selected == index.inc()) R.drawable.ic_level_fill_1 else R.drawable.ic_level_outline_1
                                    }
                                ),
                                contentDescription = stringResource(R.string.rate_x, index.inc())
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = selected != null,
                    onClick = { selected?.let { onRate(it) }; onDismiss() }
                ) {
                    Text(text = stringResource(id = R.string.rate).lowercase())
                }
            }
        )

    }
}