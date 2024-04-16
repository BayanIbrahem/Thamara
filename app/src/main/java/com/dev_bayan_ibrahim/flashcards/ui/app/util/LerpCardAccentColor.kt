package com.dev_bayan_ibrahim.flashcards.ui.app.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private const val percent = 0.25f

@Composable
fun Color.lerpSurface(): Color {
    return lerp(MaterialTheme.colorScheme.surface, percent)
}

@Composable
fun Color.lerpOnSurface(): Color {
    return lerp(MaterialTheme.colorScheme.onSurface, percent)
}

fun Color.lerp(other: Color, percent: Float = 0.5f): Color =
    Color(
        red = red.weightedAvg(other.red, percent),
        green = green.weightedAvg(other.green, percent),
        blue = blue.weightedAvg(other.blue, percent),
    )

fun Float.weightedAvg(other: Float, weight: Float = 0.5f): Float {
    return this * weight + (other * (1f - weight))

}
