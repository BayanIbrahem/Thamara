package com.dev_bayan_ibrahim.flashcards.data.model.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter.InstantConverter
import com.dev_bayan_ibrahim.flashcards.data.rank_manager.FlashRankManager
import com.dev_bayan_ibrahim.flashcards.data.util.formatWithChar
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.roundToInt

@Entity("ranks")
@TypeConverters(InstantConverter::class)
data class UserRank(
    val rank: Int,
    val exp: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val datetime: Instant = Clock.System.now()
) : Comparable<UserRank> {
    constructor(floatRank: Float) : this(
        rank = floatRank.toInt(),
        exp = (floatRank.mod(1f) * FlashRankManager.requiredExpOfRank(floatRank.toInt())).roundToInt()
    )

    companion object Companion {
        val top_rank = 18
        val min_rank = 0

        val ranks_range = min_rank..top_rank
        val Init = UserRank(min_rank, 0)

        fun calculateRankValue(rank: UserRank): Long {
            var value: Long = 0

            var r = rank.rank

            while (r > min_rank) {
                val rExp = FlashRankManager.requiredExpOfRank(r)
                value += rExp
                r -= 1
            }

            value += rank.exp

            return value
        }
    }

    @Ignore
    val requiredExp = FlashRankManager.requiredExpOfRank(rank)

    @Ignore
    val expPercent: Int = (100f * exp / requiredExp).toInt().coerceIn(0, 100)

    @Ignore
    private var _value: Long? = null

    @get:Ignore
    val value: Long
        get() = _value ?: calculateRankValue(this).also { value -> _value = value }

    override fun compareTo(other: UserRank): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String = "$rank - $expPercent%"

    @Composable
    fun asString(detailed: Boolean = true): String = buildString {
        append(rank)
        append(" - ")
        append(expPercent)
        append("%")
        if (detailed) {
            append("(")
            append(
                stringResource(
                    R.string.x_exp_of_y,
                    exp.toLong().formatWithChar(),
                    requiredExp.toLong().formatWithChar()
                )
            )
            append(")")
        }
    }

    fun asFloat(): Float = rank + (expPercent / 100f)
    operator fun inc(): UserRank = copy(rank = rank.inc().coerceAtMost(top_rank), exp = exp)
}

