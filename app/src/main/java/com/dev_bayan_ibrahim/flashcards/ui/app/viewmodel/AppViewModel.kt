package com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.ui.app.util.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repo: FlashRepo,
    networkMonitor: NetworkMonitor,
) : ViewModel() {
    private val userFlow = repo.getUser()
    val user = userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User.INVALID
    )

    val initialized = repo.initializedDb().combine(userFlow) { db, user ->
        db && (user != User.INVALID)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val isOnline = networkMonitor.isOnline.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val uiState = AppMutableUiState()
    fun getAppActions(): AppUiActions = object : AppUiActions {
        override fun onNameChange(name: String) {
            uiState.newUserName = name
        }

        override fun onAgeChange(age: Int) {
            uiState.newUserAge = age
        }

        override fun onSave() {
            viewModelScope.launch {
                repo.setUser(
                    name = uiState.newUserName,
                    age = uiState.newUserAge
                )
            }
        }
    }
}