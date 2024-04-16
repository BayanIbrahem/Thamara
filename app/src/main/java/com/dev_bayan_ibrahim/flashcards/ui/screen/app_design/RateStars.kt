package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

enum class RateStarsSize(val dp: Dp) {
    BIG(32.dp),
    MEDIUM(24.dp),
    SMALL(16.dp),
}

@Composable
fun RateStars(
    modifier: Modifier = Modifier,
    size: RateStarsSize = RateStarsSize.BIG,
    rate: Float,
    rates: Int,
    outline: Color = MaterialTheme.colorScheme.onSurface,
    fill: Color = MaterialTheme.colorScheme.primary,
) {
    val layout = LocalLayoutDirection.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(size.dp / 8)
    ) {
        repeat(5) {
            RateStar(
                size = size,
                percent = (rate - it).coerceIn(0f..1f),
                outline = outline,
                fill = fill,
                layout = layout,
            )
        }
    }
}

@Composable
private fun RateStar(
    modifier: Modifier = Modifier,
    size: RateStarsSize,
    outline: Color = MaterialTheme.colorScheme.onSurface,
    fill: Color = MaterialTheme.colorScheme.primary,
    percent: Float, // percent 0-1
    layout: LayoutDirection,
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .then(modifier),
    ) {
        Icon(
            modifier = Modifier
                .drawWithContent {
                    rtlMirror(layout) {
                        clipRect(
                            left = 0f,
                            top = 0f,
                            bottom = this.size.height,
                            right = this.size.width * percent
                        ) {
                            this@drawWithContent.drawContent()
                        }
                    }
                },
            painter = painterResource(id = R.drawable.rate_star_fill),
            contentDescription = null,
            tint = fill,
        )
        Icon(
            painter = painterResource(id = R.drawable.rate_star_outline),
            contentDescription = null,
            tint = outline,
        )
    }
}
private fun DrawScope.rtlMirror(
    layout: LayoutDirection,
    block: DrawScope.() -> Unit
) {
    scale(
        scaleX = when (layout) {
            LayoutDirection.Ltr -> 1f
            LayoutDirection.Rtl -> -1f
        },
        scaleY = 1f,
        block = block,
    )
}

@Preview(showBackground = true)
@Composable
private fun RateStarPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val fractions = 10
            LazyColumn {
                items(5 * fractions) { rate ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RateStarsSize.entries.forEach { size ->
                            RateStars(
                                size = size,
                                rate = rate / fractions.toFloat(),
                                rates = 0
                            )
                        }
                    }
                }
            }
        }
    }
}
