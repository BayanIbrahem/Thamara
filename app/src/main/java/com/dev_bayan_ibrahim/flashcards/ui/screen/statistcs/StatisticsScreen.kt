package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.LevelPlayStatistics
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.PlaysStatisticsChart
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.RankStatisticsChart
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel.StatisticsUiState


@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    state: StatisticsUiState,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatisticsItem(label = stringResource(R.string.rank_statistics)) {
            RankStatisticsChart(ranks = state.rankStatistics)
        }

        StatisticsItem(label = stringResource(R.string.play_correctness_statistics)) {
            PlaysStatisticsChart(plays = state.playsStatistics)
        }

        StatisticsItem(label = stringResource(R.string.decks_levels_count)) {
            LevelPlayStatistics(levels = state.decksLevelsStatistics,)
        }
    }
}

@Composable
private fun StatisticsItem(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        content()
    }
}