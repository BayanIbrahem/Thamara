package com.dev_bayan_ibrahim.flashcards.ui.app.util


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.delay

private val heightAnimSpec = tween<Float>(200, easing = LinearOutSlowInEasing)
private val colorAnimSpec = tween<Color>(200, easing = LinearOutSlowInEasing)

@Composable
fun ConnectivityStatusBar(
    modifier: Modifier = Modifier,
    isOnline: Boolean
) {
    var init: Boolean? by remember {
        mutableStateOf(isOnline)
    }
    val iconRes by remember(isOnline) {
        derivedStateOf{
            if (isOnline) R.drawable.ic_cloud else R.drawable.ic_cloud_off
        }
    }
    val hintRes by remember(isOnline) {
        derivedStateOf{
            if (isOnline) R.string.back_online else R.string.offline
        }
    }

    val heightAnimatable by remember {
        mutableStateOf(Animatable(0f))
    }
    val color by animateColorAsState(
        targetValue = if (isOnline) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        },
        label = "color",
        animationSpec = colorAnimSpec
    )

    val onColor by animateColorAsState(
        targetValue = if (isOnline) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onError
        },
        label = "onColor"
    )

    LaunchedEffect(key1 = isOnline) {
        if (isOnline != init) {
            init = null
            heightAnimatable.animateTo(1f, animationSpec = heightAnimSpec)
            delay(1000)
            heightAnimatable.animateTo(0.1f, animationSpec = heightAnimSpec)
            delay(5000)
            heightAnimatable.animateTo(0f, animationSpec = heightAnimSpec)
        }
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp * heightAnimatable.value)
            .drawBehind {
                drawRect(color)
            }
            .padding(1.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = onColor,
        )
        Text(
            text = stringResource(id = hintRes),
            style = MaterialTheme.typography.labelLarge,
            color = onColor
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectivityStatusBarPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            var isOnline: Boolean by remember {
                mutableStateOf(true)
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConnectivityStatusBar(isOnline = isOnline)
                Button(
                    onClick = {
                        isOnline = !isOnline
                    }
                ) {
                    Text(text = "Toggle")
                }
            }
        }
    }
}
