package com.dev_bayan_ibrahim.flashcards.ui.app.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashBottomBar(
    modifier: Modifier = Modifier,
    selected: FlashNavRoutes,
    badges: Map<FlashNavRoutes.TopLevel, Boolean> = mapOf(),
    onClick: (FlashNavRoutes.TopLevel) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        FlashNavRoutes.TopLevel.entries.forEach { screen ->
            val s by remember(selected, screen) {
                mutableStateOf(selected == screen)
            }
            NavigationBarItem(
                selected = s,
                onClick = { onClick(screen) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (badges[screen] == true) {
                                Badge {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.error)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (s) screen.selectedIconRes else screen.unselectedIconRes
                            ),
                            contentDescription = stringResource(id = screen.nameRes)
                        )
                    }
                },
                alwaysShowLabel = true,
            )
        }
    }
}
