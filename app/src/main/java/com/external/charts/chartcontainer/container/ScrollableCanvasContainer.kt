package com.external.charts.chartcontainer.container

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.LayoutDirection
import com.external.charts.chartcontainer.gestures.detectZoomGesture

/**
 *
 * ScrollableCanvasContainer is a container used to draw any graph which supports scroll,
 * zoom & tap or drag gestures.
 * @param modifier : All modifier related property.
 * @param calculateMaxDistance: callback to calculate the maximum scrolling distance.
 * @param onDraw: Draw any canvas inside the onDraw scope using the input params in the lambda fxn
 * @param drawXAndYAxis: Draw the X and Y axis along with the drawing area.
 * @param containerBackgroundColor: Background color of the whole container.
 * @param isPinchZoomEnabled: True if user can zoom in and out else false
 * @param layoutDirection: Used to define the direction of scroll.
 * @param onPointClicked: Callback for tap detected along with offset for tap.
 * @param onScroll: Callback when user starts scrolling the graph.
 * @param onZoomInAndOut: Callback when user starts zoomIn and Out w.r.t to the graph
 * @param scrollOrientation: Used to define the scroll orientation
 */

@Composable
fun ScrollableCanvasContainer(
    modifier: Modifier,
    calculateMaxDistance: DrawScope.(Float) -> Float,
    onDraw: DrawScope.(Float, Float) -> Unit,
    drawXAndYAxis: @Composable BoxScope.(Float, Float) -> Unit,
    containerBackgroundColor: Color = Color.White,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    onPointClicked: (Offset, Float) -> Unit = { _, _ -> },
    isPinchZoomEnabled: Boolean = true,
    onScroll: () -> Unit = {},
    onZoomInAndOut: () -> Unit = {},
    scrollOrientation: Orientation = Orientation.Horizontal,
    initScrollToEnd: Boolean = false,
) {
    var scrollInitialized by rememberSaveable { mutableStateOf(false) }
    val scrollOffset = rememberSaveable { mutableFloatStateOf(0f) }
    val maxScrollOffset = rememberSaveable { mutableFloatStateOf(0f) }
    val xZoom = remember { mutableFloatStateOf(1f) }
    val scrollState = rememberScrollableState { delta ->
        scrollOffset.floatValue -= delta
        scrollOffset.floatValue = checkAndGetMaxScrollOffset(
            scrollOffset.floatValue,
            maxScrollOffset.floatValue
        )
        delta
    }

    if (scrollState.isScrollInProgress) {
        onScroll()
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection
    ) {
        Box(
            modifier = modifier.clipToBounds()
        ) {
            Canvas(modifier = modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth()
                .semantics {
                    this.testTag = "chart_canvas"
                }
                .background(containerBackgroundColor)
                .scrollable(
                    state = scrollState, scrollOrientation, enabled = true
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onPointClicked(it, scrollOffset.floatValue)
                    })
                }
                .pointerInput(Unit) {
                    detectZoomGesture(
                        isZoomAllowed = isPinchZoomEnabled,
                        onZoom = { zoom ->
                            xZoom.floatValue *= zoom
                            onZoomInAndOut()
                        }
                    )
                },
                   onDraw = {
                       val maxDist = calculateMaxDistance(xZoom.floatValue)
                       if (maxDist != maxScrollOffset.floatValue || (initScrollToEnd && !scrollInitialized)) {
                           scrollOffset.floatValue = maxDist
                           scrollInitialized = true
                       }
                       maxScrollOffset.floatValue = maxDist
                       onDraw(scrollOffset.floatValue, xZoom.floatValue)
                   })
            drawXAndYAxis(scrollOffset.floatValue, xZoom.floatValue)
        }
    }
}

/**
 * Returns the scroll state within the start and computed max scrollOffset & filters invalid scroll states.
 * @param currentScrollOffset: Current scroll offset when user trying to scroll the canvas.
 * @param computedMaxScrollOffset: Maximum calculated scroll offset for given data set.
 */
fun checkAndGetMaxScrollOffset(currentScrollOffset: Float, computedMaxScrollOffset: Float): Float {
    return when {
        currentScrollOffset < 0f -> 0f
        currentScrollOffset > computedMaxScrollOffset -> computedMaxScrollOffset
        else -> currentScrollOffset
    }
}
