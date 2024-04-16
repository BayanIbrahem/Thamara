package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.ui.app.util.lerpSurface

@Composable
fun DeckCard(
    modifier: Modifier = Modifier,
    accent: Color,
    onClick: () -> Unit = {},
    enableClick: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(0.5.dp)
            .background(
                color = accent.lerpSurface(),
                shape = RoundedCornerShape(8.dp)
            ).run {
                if (enableClick) clickable(onClick = onClick) else this
            },
        contentAlignment = Alignment.Center,
        content = content
    )

}
