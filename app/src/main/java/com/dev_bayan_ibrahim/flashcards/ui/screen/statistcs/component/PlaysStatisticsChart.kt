package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.external.charts.common.model.PlotType
import com.external.charts.ui.linechart.LineChart
import com.external.charts.ui.linechart.model.LineChartData
import com.external.charts.ui.linechart.model.LinePlotData
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

@Composable
fun PlaysStatisticsChart(
    modifier: Modifier = Modifier,
    plays: List<Triple<Instant, Int, Int>>,
    primary: Color = MaterialTheme.colorScheme.primary,
    error: Color = MaterialTheme.colorScheme.error,
    onBackground: Color = MaterialTheme.colorScheme.onBackground,
    labelMediumSize: TextUnit = MaterialTheme.typography.labelMedium.fontSize,
) {
    if (plays.isNotEmpty() && plays.run { (last().first - first().first) > 3.days }) {
        val xAxisData by remember(plays) {
            derivedStateOf {
                daysXAxis(
                    items = plays,
                    neutral = onBackground,
                    getDate = { first }
                )
            }
        }
        val yAxisData by remember(plays) {
            derivedStateOf {
                flashYAxis(
                    items = plays,
                    maxItem = maxOf(plays.maxOf { it.second }, plays.maxOf { it.third }).toFloat(),
                    minItem = minOf(plays.minOf { it.second }, plays.minOf { it.third }).toFloat(),
                    onBackground = onBackground,
                )
            }
        }
        val pointsData by remember(plays) {
            derivedStateOf {
                getLinePlotData(
                    plays = plays,
                    primary = primary,
                    error = error,
                    onBackground = onBackground,
                    labelMediumSize = labelMediumSize
                )
            }
        }
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChartLineLabel(color = primary, label = stringResource(R.string.correct_answers))
                ChartLineLabel(color = error, label = stringResource(R.string.wrong_answers))
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
                initScrollToEnd = true
            )
        }
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
    plays: List<Triple<Instant, Int, Int>>,
    primary: Color,
    error: Color,
    onBackground: Color,
    labelMediumSize: TextUnit,
): LinePlotData {
    return LinePlotData(
        plotType = PlotType.Line,
        lines = listOf(
            daysChartLine(
                items = plays,
                color = error,
                onBackground = onBackground,
                labelMediumSize = labelMediumSize,
                datetime = { first },
                value = { third.toFloat() },
                popUpLabel = { _, y ->
                    y.roundToInt().toString()
                }
            ),
            daysChartLine(
                items = plays,
                color = primary,
                onBackground = onBackground,
                labelMediumSize = labelMediumSize,
                datetime = { first },
                value = { second.toFloat() },
                popUpLabel = { _, y ->
                    y.roundToInt().toString()
                }
            ),
        )
    )
}
