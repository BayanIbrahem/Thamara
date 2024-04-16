package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerpSurface

@Composable
fun getChartsColors() = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,

    Color.Yellow,
    Color.Cyan,
    Color.Magenta,

    Color(0xFF004D40),
    Color(0xFFBF360C),
    Color(0xFF880E4F),

    Color(0xFF3F51B5),
    Color(0xFFAA00FF),
    Color(0xFF8BC34A),
).map { it.lerpSurface() }
