package com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
interface AppUiActions {

    fun onNameChange(name: String)
    fun onAgeChange(age: Int)
    fun onSave()
}