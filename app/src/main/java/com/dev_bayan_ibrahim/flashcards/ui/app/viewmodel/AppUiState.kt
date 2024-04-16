package com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface AppUiState {
    val newUserName: String
    val newUserAge: Int
}

class AppMutableUiState: AppUiState {
    override var newUserName: String by mutableStateOf("")
    override var newUserAge: Int by mutableIntStateOf(10)
}