package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.util.asFormattedString
import com.dev_bayan_ibrahim.flashcards.ui.util.asSimpleDate
import com.dev_bayan_ibrahim.flashcards.ui.util.daysCount
import com.external.charts.axis.AxisData
import com.external.charts.common.model.Point
import com.external.charts.ui.linechart.model.IntersectionPoint
import com.external.charts.ui.linechart.model.Line
import com.external.charts.ui.linechart.model.LineStyle
import com.external.charts.ui.linechart.model.SelectionHighlightPoint
import com.external.charts.ui.linechart.model.SelectionHighlightPopUp
import com.external.charts.ui.linechart.model.ShadowUnderLine
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.days


fun <T> daysXAxis(
    items: List<T>,
    neutral: Color,
    getDate: T.() -> Instant
) = AxisData.Builder().apply {
    val count = items.getDaysDiff(getDate).inc()
    steps(count)
    val first = getDate(items.first())
    val labels = List(count.inc()) { i ->
        val datetime = first + i.days
        datetime.asSimpleDate()
    }
    labelData { pointIndex ->
        labels[pointIndex.coerceIn(0, count)]
    }
    axisStepSize(70.dp)
    startDrawPadding(25.dp)
    axisLineColor(neutral)
    shouldDrawAxisLineTillEnd(true)
    axisLabelColor(neutral)
    backgroundColor(Color.Transparent)
}.build()

fun <T> flashYAxis(
    items: List<T>,
    onBackground: Color,
    maxSteps: Int = 5,
    maxItem: Float,
    minItem: Float,
) = AxisData.Builder().apply {
    val range = (maxItem - minItem).run {
        if (mod(1f) > 0) inc().toInt() else roundToInt()
    }
    val steps = minOf(range, maxSteps)
    steps(steps)
    labelData {
        val percent = it / (steps.toFloat()).coerceAtLeast(1f)
        (range * percent + minItem).roundToLong().asFormattedString()
    }
    axisLineColor(onBackground)
    axisLabelColor(onBackground)
    startDrawPadding(35.dp)
    backgroundColor(Color.Transparent)
}.build()


fun <T> daysChartLine(
    items: List<T>,
    simple: Boolean = true,
    color: Color,
    onBackground: Color,
    labelMediumSize: TextUnit,
    datetime: T.() -> Instant,
    value: T.() -> Float,
    popUpLabel: (Float, Float) -> String,
) = Line(
    dataPoints = items.map { item ->
        Point(x = item.datetime().daysCount().toFloat(), y = item.value())
    },
    lineStyle = LineStyle(
        color = color,
        width = 2.dp.value,
    ),
    intersectionPoint = IntersectionPoint(
        color = color,
        radius = 4.dp
    ),
    selectionHighlightPoint = if (simple) {
        null
    } else {
        SelectionHighlightPoint(
            color = color,
            radius = 6.dp,
            isHighlightLineRequired = false,
        )
    },
    shadowUnderLine = ShadowUnderLine(
        brush = Brush.verticalGradient(
            listOf(
                color,
                Color.Transparent,
            )
        )
    ),
    selectionHighlightPopUp = if (simple) {
        null
    } else {
        SelectionHighlightPopUp(
            backgroundAlpha = 0f,
            paddingBetweenPopUpAndPoint = 10.dp,
            labelSize = labelMediumSize,
            labelColor = onBackground,
            popUpLabel = popUpLabel
        )
    }
)

private fun <T> List<T>.getDaysDiff(
    getDate: T.() -> Instant
): Int = last().getDate().daysCount() - first().getDate().daysCount()

@Composable
fun ChartLineLabel(
    modifier: Modifier = Modifier,
    color: Color,
    label: String,
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(color))
        Text(text = label, style = MaterialTheme.typography.labelLarge)
    }
}