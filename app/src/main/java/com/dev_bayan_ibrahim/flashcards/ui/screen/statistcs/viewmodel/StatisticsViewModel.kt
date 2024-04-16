package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.ui.util.fromEpochDays
import com.dev_bayan_ibrahim.flashcards.ui.util.groupByDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repo: FlashRepo,
) : ViewModel() {
    val state = StatisticsMutableUiState()

    fun initScreen() {
        viewModelScope.launch {
            val ranks = repo.getRankChangesStatistics().groupByDay {
                it.datetime
            }.mapNotNull {
                it.value.lastOrNull()
            }
            state.rankStatistics.run {
                clear()
                addAll(ranks)
            }

            val plays = repo.getPlaysStatistics().map {
                Triple(
                    it.deckPlay.datetime,
                    it.successPlays,
                    it.failedPlays,
                )
            }.groupByDay { it.first }.map { (key, value) ->
                Triple(
                    first = Instant.fromEpochDays(key),
                    second = value.sumOf { it.second },
                    third = value.sumOf { it.third },

                    )
            }
            state.playsStatistics.run {
                clear()
                addAll(plays)
            }

            state.decksLevelsStatistics.run {
                clear()
                addAll(repo.getLeveledDecksCount())
            }
        }
    }
}