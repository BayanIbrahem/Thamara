package com.external.charts.ui.linechart.model

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill

/**
 * Controls the drawing behaviour of the area/section under the line.
 *
 * @param color Color to be applied to the path
 * @param alpha Opacity to be applied to the path from 0.0f to 1.0f representing
 * fully transparent to fully opaque respectively
 * @param style Whether or not the path is stroked or filled in
 * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
 * @param blendMode Blending algorithm to be applied to the path when it is drawn
 * @param draw override this to change the default drawPath implementation. You are provided with
 * the [Path] of the line
 * @param drawMultiColor override this to change the default drawPath implementation. You are provided with
 * the [Path] of the line, custom [Color] and [Brush]
 */
data class ShadowUnderLine(
    val color: Color = Color.Black,
    val brush: Brush? = null,
    val alpha: Float = 0.1f,
    val style: DrawStyle = Fill,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode,
    val draw: DrawScope.(Path) -> Unit = { path ->
        if (brush != null) {
            drawPath(path, brush, alpha, style, colorFilter, blendMode)
        } else {
            drawPath(path, color, alpha, style, colorFilter, blendMode)
        }
    },
    val drawMultiColor: DrawScope.(Path, Color, Brush?) -> Unit = { path, multiColor, multiColorBrush ->
        if (multiColorBrush != null) {
            drawPath(path, multiColorBrush, alpha, style, colorFilter, blendMode)
        } else {
            drawPath(path, multiColor, alpha, style, colorFilter, blendMode)
        }
    }
)
