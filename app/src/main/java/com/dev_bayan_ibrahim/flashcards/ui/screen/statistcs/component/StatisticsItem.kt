package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun StatisticsItem(
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    label: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = it),
                contentDescription = null
            )
        }
        Text(
            modifier = modifier,
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        content()
    }
}
@Composable
fun StatisticsItem(
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    label: String,
    value: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = it),
                contentDescription = null
            )
        }
        Text(
            modifier = modifier,
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = modifier,
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
