package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.util.asFormattedString
import com.external.charts.axis.AxisData
import com.external.charts.axis.DataCategoryOptions
import com.external.charts.common.model.Point
import com.external.charts.ui.barchart.models.BarData
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun barXAxis(
    valuesCount: Int,
    valueLabel: Int.() -> String,
    neutral: Color,
): AxisData = AxisData.Builder().apply {
    steps(valuesCount)
    labelData(valueLabel)
    axisLineColor(neutral)
    endPadding(0.dp)
    topPadding(35.dp)
    axisLabelColor(neutral)
    startDrawPadding(25.dp)
    backgroundColor(Color.Transparent)
}.build()

fun barYAxis(
    maxValue: Float,
    minValue: Float,
    maxSteps: Int = 5,
    neutral: Color,
): AxisData = AxisData.Builder().apply {
    val range = (maxValue - minValue).run {
        if (mod(1f) > 0) inc().toInt() else roundToInt()
    }
    val steps = minOf(maxSteps, range)
    steps(steps)
    labelData {
        val percent = it / (steps.toFloat()).coerceAtLeast(1f)
        (range * percent + minValue).roundToLong().asFormattedString()
    }
    axisLineColor(neutral)
    axisLabelColor(neutral)
    axisOffset(35.dp)
    backgroundColor(Color.Transparent)
}.build()

fun barChartData(
    values: Iterable<Pair<Number, Number>>,
    colors: List<Color>,
) = values.mapIndexed { i, (x, y) ->
    val color = colors[i.mod(colors.count())]
    BarData(
        point = Point(x.toFloat(), y.toFloat()),
        gradientColorList = listOf(color.copy(alpha = 0.5f), color),
        label = y.toInt().toString(),
        dataCategoryOptions = DataCategoryOptions(),
    )
}