package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DynamicText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = 1,
    minLines: Int = maxLines,
) {
    var adaptedStyle by remember {
        mutableStateOf(style)
    }
    var finished by remember {
        mutableStateOf(false)
    }

    Text(
        modifier = modifier
            .drawWithContent {
                if (finished) {
                    drawContent()
                }
            },
        text = text,
        style = adaptedStyle,
        textAlign = textAlign,
        onTextLayout = {
            if (
                it.lineCount > maxLines
                || it.didOverflowHeight
                || it.didOverflowWidth
            ) {
                adaptedStyle = adaptedStyle.copy(
                    fontSize = adaptedStyle.fontSize * 0.9f,
                    lineHeight = adaptedStyle.lineHeight * 0.9f,
                )
            } else if (
                it.lineCount < minLines && adaptedStyle.fontSize < style.fontSize
            ) {
                val newSize = adaptedStyle.fontSize * 1.1f
                val newLineHeight = adaptedStyle.lineHeight * 1.1f
                adaptedStyle = adaptedStyle.copy(
                    fontSize = if (newSize > style.fontSize) style.fontSize else newSize,
                    lineHeight = if (newLineHeight > style.lineHeight) style.lineHeight else newLineHeight,
                )
            } else {
                finished = true
            }
        }
    )
}
