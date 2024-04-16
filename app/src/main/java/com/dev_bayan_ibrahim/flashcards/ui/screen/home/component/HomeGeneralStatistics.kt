@file:OptIn(ExperimentalLayoutApi::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.home.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.chart.AccuracyAvgChart
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.StatisticsItem
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun HomeGeneralStatistics(
    modifier: Modifier = Modifier,
    statistics: GeneralStatistics

) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.accuracy_average),
        ) {
            AccuracyAvgChart(
                modifier = Modifier.fillMaxWidth(0.5f),
                correctAnswers = statistics.correctAnswers,
                failedAnswers = statistics.failedAnswers
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGeneralStatisticsPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeGeneralStatistics(
                statistics = GeneralStatistics(
                    correctAnswers = 10,
                    failedAnswers = 10,
                )
            )
        }
    }
}
