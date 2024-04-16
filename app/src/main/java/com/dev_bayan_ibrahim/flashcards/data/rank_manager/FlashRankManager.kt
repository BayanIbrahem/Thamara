package com.dev_bayan_ibrahim.flashcards.data.rank_manager

import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank

data class PlayCardResult(
    val new: Boolean,
    val correct: Boolean,
)

object FlashRankManager {

    private const val levelModifier: Int = 4

    private const val correctFirstTimeModifier: Int = 4
    private const val correctNotFirstTimeModifier: Int = 1

    private const val incorrectFirstTimeModifier: Int = -2
    private const val incorrectNotFirstTimeModifier: Int = -1

    fun calculateNewRank(
        oldRank: UserRank,
        deckLevel: Int,
        cards: List<PlayCardResult>
    ): UserRank {
        var newExp = oldRank.exp
        var newRank = oldRank.rank

        for (cardResult in cards) {
            val expModifier = getExpModifier(cardResult)
            val expChange = getExpChangeForCard(deckLevel, expModifier)
            newExp += expChange
        }

        while (newExp < 0) {
            newRank -= 1
            if (newRank < 0) return UserRank(0, 0)
            newExp += requiredExpOfRank(newRank)
        }

        while (newExp > requiredExpOfRank(newRank)) {
            newExp -= requiredExpOfRank(newRank)
            newRank += 1
        }

        return UserRank(newRank, newExp)
    }

    private fun getExpModifier(
        cardResult: PlayCardResult
    ): Int {
        return if (cardResult.correct) {
            when (cardResult.new) {
                true -> correctFirstTimeModifier
                false -> correctNotFirstTimeModifier
            }
        } else {
            when (cardResult.new) {
                true -> incorrectFirstTimeModifier
                false -> incorrectNotFirstTimeModifier
            }
        }
    }

    private fun getExpChangeForCard(
        deckLevel: Int,
        expModifier: Int
    ): Int {
        return (deckLevel * levelModifier * expModifier)
    }

    fun requiredExpOfRank(rank: Int): Int = (rank * 1000).coerceAtLeast(1)
}