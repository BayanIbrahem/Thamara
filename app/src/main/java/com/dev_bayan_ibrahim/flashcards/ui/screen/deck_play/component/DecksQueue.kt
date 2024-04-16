package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.ExpandedCard

private const val fadeAnim = 1_000
val exitAnim = fadeOut(
    animationSpec = tween(fadeAnim)
) + scaleOut(
    animationSpec = tween(fadeAnim),
    targetScale = 1.1f
)
val rotationAnim = tween<Float>(100, fadeAnim / 2)
@Composable
fun DecksQueue(
    modifier: Modifier = Modifier,
    deck: Deck,
    currentIndex: Int,
    onSelectAnswer: (String) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        repeat(deck.header.cardsCount) {
            val i = deck.header.cardsCount.dec() - it
            val card = deck.cards[i]
            if (i - currentIndex in -1..10) {
                AnimatedVisibility(
                    visible = i >= currentIndex,
                    exit = exitAnim
                ) {
                    val rotationAnimatable by animateFloatAsState(
                        targetValue = if (i == currentIndex) {
                            0f
                        } else {
                            45f * i / deck.header.cardsCount
                            // always last card has 45 degree rotation
                        },
                        animationSpec = rotationAnim,
                        label = "",
                    )
                    ExpandedCard(
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotationAnimatable
                            },
                        card = card,
                        accent = deck.header.colorAccent,
                        bgPattern = deck.header.pattern,
                        showContent = i - currentIndex in -1..1,
                        clickable = rotationAnimatable == 0f && i == currentIndex,
                        onSelectAnswer = onSelectAnswer
                    )
                }
            }
        }
    }
}