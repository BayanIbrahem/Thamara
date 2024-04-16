package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: FlashRepo,
) : ViewModel() {
    val state = HomeMutableUiState()
    val user = repo
        .getUser()
        .combine(repo.getTotalPlaysCount()) { user, totalPlays ->
            user?.copy(totalPlays = totalPlays)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val generalStatistics = repo
        .getGeneralStatistics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GeneralStatistics()
        )

    fun getUiActions(): HomeUiActions = object : HomeUiActions {

    }
}