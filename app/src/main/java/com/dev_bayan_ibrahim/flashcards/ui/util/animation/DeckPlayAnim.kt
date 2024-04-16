package com.dev_bayan_ibrahim.flashcards.ui.util.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

fun deckPlaEnterAnim(): EnterTransition = fadeIn(
    animationSpec = tweenFloat500
)

fun deckPlaExitAnim(): ExitTransition = fadeOut(
    animationSpec = tweenFloat500
)
