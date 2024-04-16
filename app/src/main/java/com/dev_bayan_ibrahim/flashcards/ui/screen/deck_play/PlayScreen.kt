package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.CancelPlayDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.DecksQueue
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.PlayResults
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.StartScreen
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiState
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.deckPlaEnterAnim
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.deckPlaExitAnim

@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    state: PlayUiState,
    actions: PlayUiActions,
) {
    BackHandler (onBack = actions::onBackHandlerClick)
    CancelPlayDialog(
        show = state.showCancelPlayDialog,
        onCancelPlay = actions::onCancelPlay,
        onContinuePlay = actions::onContinuePlay,
    )
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = state.status == PlayStatus.NOT_STARTED,
            enter = deckPlaEnterAnim(),
            exit = deckPlaExitAnim(),
        ) {
            StartScreen(
                onStart = actions::onStartPlay,
                deckHeader = state.deck.header
            )
        }

        AnimatedVisibility(
            visible = state.status == PlayStatus.PLAYING,
            enter = deckPlaEnterAnim(),
            exit = deckPlaExitAnim(),
        ) {
            DecksQueue(
                deck = state.deck,
                currentIndex = state.currentCard,
                onSelectAnswer = actions::onSelectAnswer
            )
        }

        AnimatedVisibility(
            visible = state.status == PlayStatus.RESULTS,
            enter = deckPlaEnterAnim(),
            exit = deckPlaExitAnim(),
        ) {
            PlayResults(
                count = state.deck.cards.count(),
                correctAnswers = state.correctAnswers,
                incorrectCards = state.cardsAnswers,
                accent = state.deck.header.colorAccent,
                bgPattern = state.deck.header.pattern,
                isRateLoading = state.isRateLoading,
                onRepeat = actions::onRepeat,
                onClose = actions::onClose,
                onRate = actions::onRate,
            )
        }
    }
}

