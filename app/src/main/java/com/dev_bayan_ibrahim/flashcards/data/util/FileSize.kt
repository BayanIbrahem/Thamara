package com.dev_bayan_ibrahim.flashcards.data.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dev_bayan_ibrahim.flashcards.R
import kotlin.math.absoluteValue
import kotlin.math.pow

fun Long.formatWithChar(caps: Boolean = false): String = when (this.absoluteValue) {
    in 0..999 -> "$this"
    in 1_000..999_000 -> "${this / 1_000}k"
    in 1_000_000..999_000_000 -> "${this / 1_000_000}m"
    in 1_000_000_000..999_000_000_000 -> "${this / 1_000_000_000}g"
    in 1_000_000_000_000..999_000_000_000_000 -> "${this / 1_000_000_000_000}t"
    in 1_000_000_000_000_000..999_000_000_000_000_000 -> "${this / 1_000_000_000_000_000}p"

    else -> {
        val len = this.toString().length
        val x = this / 10f.pow(len.dec())
        "${x}e${len.dec()}"
    }
}.run { if (caps) uppercase() else this }

fun Long.toFormattedSize(
): FileFormattedSize {
    require(this >= 0) {
        Log.e("formatting size", "error formatting size $this (must be non negative value)")
    }
    return if (this <= 1024) { // < 2^10  byte
        FileFormattedSize.Byte(this)
    } else if (this <= 1_048_576) { // < 2^20 kilo byte
        FileFormattedSize.KiloByte(this / 1024)
    } else if (this <= 1_073_741_824) { // < 2^30 mega byte
        FileFormattedSize.MegaByte(this / 1_048_576)
    } else if (this <= 1_099_511_627_776) { // < 2^40 giga byte
        FileFormattedSize.GigaByte(this / 1.07374182E9.toLong())
    } else { // > 2^40 tera byte
        FileFormattedSize.TeraByte(this / 1.09951163E12.toLong())
    }
}

sealed class FileFormattedSize(
    open val value: Long,
    @StringRes val shortUnitRes: Int,
    @StringRes val longUnitRes: Int,
) {
    data class Byte(override val value: Long) :
        FileFormattedSize(value, R.string.byte_short, R.string.byte_long)

    data class KiloByte(override val value: Long) :
        FileFormattedSize(value, R.string.kilobyte_short, R.string.kilobyte_long)

    data class MegaByte(override val value: Long) :
        FileFormattedSize(value, R.string.megabyte_short, R.string.megabyte_long)

    data class GigaByte(override val value: Long) :
        FileFormattedSize(value, R.string.gigabyte_short, R.string.gigabyte_long)

    data class TeraByte(override val value: Long) :
        FileFormattedSize(value, R.string.terabyte_short, R.string.terabyte_long)

    @Composable
    fun getValue(short: Boolean = true): String = if (short) {
        stringResource(id = shortUnitRes, "$value")

    } else {
        stringResource(id = longUnitRes, "$value")
    }
}
