package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.chart


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.external.charts.common.model.PlotType
import com.external.charts.ui.piechart.charts.PieChart
import com.external.charts.ui.piechart.models.PieChartConfig
import com.external.charts.ui.piechart.models.PieChartData
import kotlin.random.Random

@Composable
fun AccuracyAvgChart(
    modifier: Modifier = Modifier,
    correctAnswers: Int,
    failedAnswers: Int,
) {
    val sum by remember(
        correctAnswers,
        failedAnswers
    ) { derivedStateOf { correctAnswers + failedAnswers } }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (sum > 0) {
            PieChart(
                modifier = Modifier,
                pieChartData = getChartData(
                    correctAnswers = correctAnswers,
                    failedAnswers = failedAnswers,
                ),
                pieChartConfig = getChartConfig(),
            )
        } else {
            Text(
                text = stringResource(id = R.string.no_data),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun getChartConfig(): PieChartConfig = PieChartConfig(
    sliceLabelTextColor = MaterialTheme.colorScheme.onSurface,
    backgroundColor = Color.Transparent,
    isAnimationEnable = true,
    labelVisible = true,
    labelType = PieChartConfig.LabelType.PERCENTAGE,
    isEllipsizeEnabled = true,
    chartPadding = 0,
    isClickOnSliceEnabled = false,
)

@Composable
private fun getChartData(
    correctAnswers: Int,
    failedAnswers: Int,
): PieChartData {
    return PieChartData(
        slices = listOf(
            PieChartData.Slice(
                label = stringResource(R.string.correct),
                value = correctAnswers.toFloat(),
                color = MaterialTheme.colorScheme.primaryContainer
            ),
            PieChartData.Slice(
                label = stringResource(R.string.failed),
                value = failedAnswers.toFloat(),
                color = MaterialTheme.colorScheme.errorContainer
            ),
        ),
        plotType = PlotType.Pie,
    )
}


@Preview(showBackground = true, device = "id:pixel_8")
@Composable
private fun TagsStatisticsChartPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            TagsStatisticsChart(
                tags = List<Pair<String?, Int>>(9) {
                    "tag $it" to Random.nextInt(1, 10)
                }.toMap().toMutableMap().run {
                    this[null] = 10
                    this
                },
            )
        }
    }
}
