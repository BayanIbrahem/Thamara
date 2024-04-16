package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.di.AppInstallation
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.rank_manager.FlashRankManager
import com.dev_bayan_ibrahim.flashcards.data.rank_manager.PlayCardResult
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarMessages
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.app.util.getThrowableMessage
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val repo: FlashRepo,
    appInstallationMonitor: AppInstallation,
) : ViewModel() {
    val state = PlayMutableUiState()
    val deviceId = appInstallationMonitor.id

    fun initScreen(deckId: Long) {
        if (deckId != state.deck.header.id) {
            viewModelScope.launch {
                state.deck = repo.getDeckCards(deckId)
            }
        }
    }

    private suspend fun getDeviceId() = try {
        FirebaseMessaging.getInstance().token.await()
        val deviceId = deviceId.first()
        Log.d("device_id", deviceId)
        deviceId
    } catch (e: Exception) {
        null
    }


    private fun handleAnswer(card: Card, answer: String) {
        if (state.cardsAnswers.all { it.first.id != card.id }) { // check not to add a card twice
            val ans = if (card.answer.checkIfCorrect(answer)) {
                state.correctAnswers += 1
                null
            } else {
                answer
            }
            state.cardsAnswers.add(card to ans)
        }
    }

    private fun handleNextCard() {
        if (state.currentCard == state.deck.cards.count().dec()) {
            state.status = PlayStatus.RESULTS
            handleSavePlayRecord()
        } else {
            state.currentCard += 1
        }
    }

    private suspend fun calculateAndSaveNewRank() {
        val oldRank = repo.getUser().first()?.rank ?: return
        val firstPlay = repo.isFirstPlay(state.deck.header.id)
        val newRank = FlashRankManager.calculateNewRank(
            oldRank = oldRank,
            deckLevel = state.deck.header.level,
            cards = state.cardsAnswers.map { (_, wrongAnswer) ->
                PlayCardResult(
                    new = firstPlay,
                    correct = wrongAnswer == null
                )
            }
        )
        repo.updateUserRank(newRank)
    }

    private fun handleSavePlayRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            calculateAndSaveNewRank()
            repo.saveDeckResults(
                state.deck.header.id,
                state.cardsAnswers.associate { (card, answer) ->
                    card.id to (answer == null)
                },
            )

        }
    }

    fun getUiActions(
        navigateUp: () -> Unit,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
    ): PlayUiActions = object : PlayUiActions {
        override fun onSelectAnswer(value: String) {
            val card = state.deck.cards[state.currentCard]
            handleAnswer(card, value)
            handleNextCard()
        }

        override fun onStartPlay() {
            state.status = PlayStatus.PLAYING
        }

        override fun onRepeat() {
            state.status = PlayStatus.NOT_STARTED
            state.currentCard = 0
            state.correctAnswers = 0
            state.cardsAnswers.clear()
        }

        override fun onClose() = navigateUp()

        override fun onBackHandlerClick() {
            when (state.status) {
                PlayStatus.PLAYING -> {
                    state.showCancelPlayDialog = true
                }

                else -> navigateUp()
            }
        }

        override fun onRate(rate: Int) {
            state.isRateLoading = true
            viewModelScope.launch {
                getDeviceId()?.let { deviceId ->
                    repo.rateDeck(
                        id = state.deck.header.id,
                        rate = rate,
                        deviceId = deviceId
                    ).fold(
                        onSuccess = {
                            onShowSnackbarMessage(FlashSnackbarMessages.UnknownDeviceRateFailed)
                        },
                        onFailure = { throwable ->
                            onShowSnackbarMessage(
                                FlashSnackbarMessages.Factory(
                                    actionLabel = { null },
                                    message = { context ->
                                        getThrowableMessage(
                                            context = context,
                                            throwable = throwable,
                                            onStatusCode = {
                                                val code = it.response.status.value
                                                val message = it.message ?: return@getThrowableMessage null
                                                if (code == 400 && message.contains("لقد قمت بالتقييم")) {
                                                    context.getString(FlashSnackbarMessages.DuplicateRateError.message)
                                                } else {
                                                    null
                                                }
                                            }
                                        )
                                    },
                                )
                            )
                        }
                    )
                } ?: onShowSnackbarMessage(FlashSnackbarMessages.UnknownDeviceRateFailed)
                state.isRateLoading = false
            }
        }

        override fun onCancelPlay() {
            state.showCancelPlayDialog = false
            navigateUp()
        }

        override fun onContinuePlay() {
            state.showCancelPlayDialog = false
        }

    }

    private suspend fun rateDeck(id: Long, rate: Int) {
        getDeviceId()?.let { deviceId ->
            repo.rateDeck(id, rate, deviceId)
        }
    }

    private var enableBackButton: Boolean = false

    private var enableBackButtonJob: Job? = null
    private fun updateBackButtonEnabled() {
        enableBackButtonJob?.cancel()
        enableBackButtonJob = viewModelScope.launch {
            enableBackButton = true
            delay(500)
            enableBackButton = false
        }
    }

    fun onBackRequest(
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
        navigateUp: () -> Unit
    ) {
        if (enableBackButton) {
            navigateUp()
        } else {
        }
        updateBackButtonEnabled()

    }
}