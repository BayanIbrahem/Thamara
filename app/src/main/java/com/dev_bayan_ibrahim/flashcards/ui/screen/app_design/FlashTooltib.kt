package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerpOnSurface
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerpSurface
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.launch

object TooltibPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val bottom = windowSize.height - anchorBounds.bottom > popupContentSize.height
        val y =
            if (bottom) anchorBounds.bottom else (anchorBounds.top - popupContentSize.height).coerceAtLeast(
                0
            )

        val anchorMid = anchorBounds.run { right + left } / 2
        val originalX = anchorMid - popupContentSize.width / 2

        if (originalX <= 0) {
            return IntOffset(0, y)
        }
        val diff = originalX + popupContentSize.width - windowSize.width
        if (diff > 0) {
            return IntOffset(
                x = originalX - diff,
                y = y
            )
        }
        return IntOffset(originalX, y)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Color?.getTooltipColor(): RichTooltipColors = RichTooltipColors(
    containerColor = this?.lerpSurface() ?: MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    titleContentColor = this?.lerpOnSurface() ?: MaterialTheme.colorScheme.onSurface,
    actionContentColor = this?.lerpOnSurface() ?: MaterialTheme.colorScheme.onSurface,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashTooltib(
    modifier: Modifier = Modifier,
    state: TooltipState,
    tint: Color? = null,
    tooltipTitle: (@Composable () -> Unit)? = null,
    tooltipAction: (@Composable () -> Unit)? = null,
    tooltipContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltibPositionProvider,
        tooltip = {
            RichTooltip(
                colors = tint.getTooltipColor(),
                title = tooltipTitle,
                action = tooltipAction,
                text = tooltipContent,
            )
        },
        state = state,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashTooltipPreview(
    tooltip: @Composable (state: TooltipState, content: @Composable () -> Unit) -> Unit
) {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val state = rememberTooltipState(
                isPersistent = true
            )
            val scope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                tooltip(state) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                state.show()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FlashToolTibPreviewLight() {
    FlashTooltipPreview { state, content ->
        FlashTooltib(
            state = state,
            tooltipTitle = {
                Text("tooltip title")
            },
            tooltipAction = {
                Text("tooltip action")
            },
            tooltipContent = {
                Text("tooltip content")
            },
            content = content,
        )
    }
}
