@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun FlashLazyRowTooltip(
    modifier: Modifier = Modifier,
    state: TooltipState,
    itemWidth: Dp,
    visibleItemsCount: Int,
    currentItemIndex: Int,
    items: LazyListScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    FlashTooltib(
        modifier = modifier,
        state = state,
        tooltipContent = {
            TooltipLazyList(
                itemWidth = itemWidth,
                visibleItemsCount = visibleItemsCount,
                currentItemIndex = currentItemIndex,
                items = items,
            )
        },
        content = content,
    )
}


@Composable
private fun TooltipLazyList(
    modifier: Modifier = Modifier,
    itemWidth: Dp,
    visibleItemsCount: Int,
    currentItemIndex: Int,
    items: LazyListScope.() -> Unit,
) {
    val state = rememberLazyListState()
    var animated by rememberSaveable {
        mutableStateOf(false)
    }

    LazyRow(
        modifier = modifier.width(
            itemWidth * visibleItemsCount +
                    (8.dp * visibleItemsCount.dec().coerceAtLeast(0))
        ),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = items,
    )

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        if (!animated) {
            val itemsCount = state.layoutInfo.totalItemsCount
            val firstVisibleItem =
                (currentItemIndex - visibleItemsCount / 2).coerceIn(0..itemsCount)

            val firstVisibleOffset =
                if (visibleItemsCount % 2 == 0 && firstVisibleItem > currentItemIndex) {
                    itemWidth.value.roundToInt() / 2
                } else 0

            state.animateScrollToItem(
                index = firstVisibleItem,
                scrollOffset = firstVisibleOffset
            )
            animated = true
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashLazyRowTooltipPreviewLight() {
    FlashTooltipPreview { state, content ->
        val current = 10
        FlashLazyRowTooltip(
            state = state,
            itemWidth = 48.dp,
            visibleItemsCount = 4,
            currentItemIndex = current,
            items = {
                items(50) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .drawBehind {
                                if (it == current) {
                                    drawCircle(Color.Blue)
                                } else {
                                    drawCircle(Color.Red)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = it.toString())
                    }
                }
            },
            content = content,
        )
    }
}
