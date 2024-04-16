package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.external.charts.common.model.PlotType
import com.external.charts.ui.linechart.LineChart
import com.external.charts.ui.linechart.model.LineChartData
import com.external.charts.ui.linechart.model.LinePlotData
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@Composable
fun RankStatisticsChart(
    modifier: Modifier = Modifier,
    ranks: List<UserRank>,
    secondary: Color = MaterialTheme.colorScheme.secondary,
    neutral: Color = MaterialTheme.colorScheme.onBackground,
    labelMediumSize: TextUnit = MaterialTheme.typography.labelMedium.fontSize,
) {
    if (ranks.isNotEmpty() && ranks.run { (last().datetime - first().datetime) > 3.days }) {
        val xAxisData by remember(ranks) {
            derivedStateOf {
                daysXAxis(
                    items = ranks,
                    neutral = neutral,
                    getDate = { datetime }
                )
            }
        }
        val yAxisData by remember(ranks) {
            derivedStateOf {
                flashYAxis(
                    items = ranks,
                    maxItem = ranks.maxOf { it.asFloat() },
                    minItem = ranks.minOf { it.asFloat() },
                    onBackground = neutral,
                )
            }
        }
        val pointsData by remember(ranks) {
            derivedStateOf {
                getLinePlotData(
                    ranks = ranks,
                    color = secondary,
                    neutral = neutral,
                    labelMediumSize = labelMediumSize
                )
            }
        }
        LineChart(
            modifier = modifier
                .height(200.dp)
                .fillMaxWidth(),
            lineChartData = LineChartData(
                linePlotData = pointsData,
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                isZoomAllowed = false,
                backgroundColor = Color.Transparent,
                containerPaddingEnd = 50.dp
            ),
            initScrollToEnd = true,
        )
    } else {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

private fun getLinePlotData(
    ranks: List<UserRank>,
    color: Color,
    neutral: Color,
    labelMediumSize: TextUnit,
): LinePlotData {
    return LinePlotData(
        plotType = PlotType.Line,
        lines = listOf(
            daysChartLine(
                items = ranks,
                simple = false,
                color = color,
                onBackground = neutral,
                labelMediumSize = labelMediumSize,
                datetime = {datetime},
                value = {asFloat()},
                popUpLabel = { _, y ->
                    UserRank(y).toString()
                }
            ),
        )
    )
}


@Preview(showBackground = true)
@Composable
private fun RankStatisticsChartPreview() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val now = Clock.System.now()
            RankStatisticsChart(
                ranks = listOf(
                    UserRank(1, 1, datetime = now - 365.days),
                    UserRank(9, 2, datetime = now - 300.days),
                    UserRank(7, 1, datetime = now - 250.days),
                    UserRank(2, 10, datetime = now - 100.days),
                    UserRank(3, 50, datetime = now - 50.days),
                    UserRank(5, 32, datetime = now - 10.days),
                )
            )
        }
    }
}
