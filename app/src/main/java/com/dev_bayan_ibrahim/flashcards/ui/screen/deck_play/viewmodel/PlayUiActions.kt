package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
interface PlayUiActions {
    fun onSelectAnswer(value: String)
    fun onStartPlay()
    fun onRepeat()
    fun onClose()
    fun onCancelPlay()

    fun onContinuePlay()
    fun onBackHandlerClick()
    fun onRate(rate: Int)
}