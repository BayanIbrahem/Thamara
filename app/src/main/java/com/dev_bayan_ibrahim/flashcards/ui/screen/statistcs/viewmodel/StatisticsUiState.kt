package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import kotlinx.datetime.Instant

@Stable
interface StatisticsUiState {
    val rankStatistics: List<UserRank>
    val playsStatistics: List<Triple<Instant, Int, Int>>
    val decksLevelsStatistics: List<Pair<Int, Int>>
}

class StatisticsMutableUiState : StatisticsUiState {
    override val rankStatistics: SnapshotStateList<UserRank> = mutableStateListOf()
    override val playsStatistics: SnapshotStateList<Triple<Instant, Int, Int>> = mutableStateListOf()
    override val decksLevelsStatistics: SnapshotStateList<Pair<Int, Int>> = mutableStateListOf()
}