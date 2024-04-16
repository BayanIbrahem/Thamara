package com.external.charts.ui.piechart

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.external.charts.common.model.PlotType
import com.external.charts.ui.piechart.charts.DonutPieChart
import com.external.charts.ui.piechart.charts.PieChart
import com.external.charts.ui.piechart.models.PieChartConfig
import com.external.charts.ui.piechart.models.PieChartData

@OptIn(ExperimentalMaterialApi::class)
object ChartWrapper {

    /**
     * Wrapper compose method for drawing Pie Chart and Donut chart.
     * @param modifier : All modifier related property
     * @param plotType: Type of the chart (Pie or Donut)
     * @param pieChartData: data list for the pie chart
     * @param onSliceClick: Callback when any slice is clicked.
     */

    @Composable
    fun DrawChart(
        modifier: Modifier,
        plotType: PlotType,
        pieChartData: PieChartData,
        pieChartConfig: PieChartConfig,
        onSliceClick: (PieChartData.Slice) -> Unit = {}
    ) {
        when (plotType) {
            is PlotType.Pie -> {
                PieChart(
                    modifier = modifier,
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig,
                    onSliceClick = onSliceClick
                )
            }
            is PlotType.Donut -> {
                DonutPieChart(
                    modifier = modifier,
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig,
                    onSliceClick = onSliceClick
                )
            }
            else -> { // T0DO Handle if required for other types
            }
        }
    }
}
