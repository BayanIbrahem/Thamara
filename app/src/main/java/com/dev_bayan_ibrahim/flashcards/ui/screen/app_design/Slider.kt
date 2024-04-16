package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashSlider(
    modifier: Modifier = Modifier,
    value: Float,
    steps: Int,
    readonly: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    // TODO improve the visual layout of the indicator
    val indicatorColor = MaterialTheme.colorScheme.primary
    val indicatorStyle = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary
    )
    val valueTextMeasurer = rememberTextMeasurer()
    var showIndicator by rememberSaveable {
        mutableStateOf(false)
    }
    val onChange: (Float) -> Unit = remember {
        {
            showIndicator = true
            onValueChange(it)
        }
    }
    val onChangeFinished: () -> Unit = remember {
        {
            showIndicator = false
            onValueChangeFinished()
        }
    }
    Slider(
        modifier = modifier,
        thumb = {
            if (!readonly) {
                SliderDefaults.Thumb(
                    modifier = Modifier.drawWithCache {
                        val measuredText = valueTextMeasurer.measure(
                            text = "${value.toInt()}",
                            style = indicatorStyle
                        )
                        onDrawBehind {
                            if (showIndicator) {
                                val indicatorCenterY = -size.height - 16.dp.toPx()
                                val textY = indicatorCenterY - (measuredText.size.height / 2)
                                val textX = center.x - (measuredText.size.width / 2)
                                drawCircle(
                                    color = indicatorColor.copy(alpha = 0.16f),
                                    center = center,
                                    radius = 24.dp.toPx()
                                )
                                drawCircle(
                                    color = indicatorColor,
                                    center = center.copy(y = indicatorCenterY),
                                    radius = 16.dp.toPx()
                                )
                                drawText(
                                    textLayoutResult = measuredText,
                                    topLeft = center.copy(x = textX, y = textY)
                                )
                            }
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                )
            }
        },
        value = value,
        onValueChange = onChange,
        onValueChangeFinished = onChangeFinished,
        valueRange = valueRange,
        steps = steps,
        colors = if (readonly) {
            SliderDefaults.colors(
                inactiveTrackColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            )
        } else SliderDefaults.colors()
    )
}

fun IntRange.toFloatRange(): ClosedFloatingPointRange<Float> = first.toFloat()..last.toFloat()
fun ClosedFloatingPointRange<Float>.toIntRange(): IntRange = start.roundToInt()..endInclusive.roundToInt()
@JvmName("IntRangeLength")
fun IntRange.range(): Int = last - first
@JvmName("FloatRangeLength")
fun ClosedFloatingPointRange<Float>.range(): Float = endInclusive - start

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashRangeSlider(
    modifier: Modifier = Modifier,
    steps: Int,
    selectedRange: ClosedFloatingPointRange<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    var startIndicator by remember { mutableStateOf(false) }
    var endIndicator by remember { mutableStateOf(false) }

    val state by remember {
        derivedStateOf {
            RangeSliderState(
                steps = steps.coerceAtLeast(0),
                activeRangeStart = selectedRange.start,
                activeRangeEnd = selectedRange.endInclusive,
                valueRange = valueRange,
                onValueChangeFinished = {
                    startIndicator = false
                    endIndicator = false
                }
            )
        }
    }
    LaunchedEffect(key1 = state.activeRangeStart) {
        startIndicator = true
        onValueChange(state.activeRangeStart..state.activeRangeEnd)
    }

    LaunchedEffect(key1 = state.activeRangeEnd) {
        endIndicator = true
        onValueChange(state.activeRangeStart..state.activeRangeEnd)
    }

    LaunchedEffect(key1 = Unit) {
        startIndicator = false
        endIndicator = false
    }

    RangeSlider(
        modifier = modifier,
        startThumb = {
            SliderThumb(value = it.activeRangeStart, showIndicator = startIndicator)
        },
        endThumb = {
            SliderThumb(value = it.activeRangeEnd, showIndicator = endIndicator)
        },
        state = state,
    )
}

@Composable
private fun SliderThumb(
    modifier: Modifier = Modifier,
    value: Float,
    showIndicator: Boolean,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    indicatorStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary
    ),
    valueTextMeasurer: TextMeasurer = rememberTextMeasurer()
) {
    SliderDefaults.Thumb(
        modifier = modifier.drawWithCache {
            val measuredText = valueTextMeasurer.measure(
                text = "${value.roundToInt()}",
                style = indicatorStyle
            )
            onDrawBehind {
                if (showIndicator) {
                    val indicatorCenterY = -size.height - 16.dp.toPx()
                    val textY = indicatorCenterY - (measuredText.size.height / 2)
                    val textX = center.x - (measuredText.size.width / 2)
                    drawCircle(
                        color = indicatorColor.copy(alpha = 0.16f),
                        center = center,
                        radius = 24.dp.toPx()
                    )
                    drawCircle(
                        color = indicatorColor,
                        center = center.copy(y = indicatorCenterY),
                        radius = 16.dp.toPx()
                    )
                    drawText(
                        textLayoutResult = measuredText,
                        topLeft = center.copy(x = textX, y = textY)
                    )
                }
            }
        },
        interactionSource = remember { MutableInteractionSource() },
    )
}