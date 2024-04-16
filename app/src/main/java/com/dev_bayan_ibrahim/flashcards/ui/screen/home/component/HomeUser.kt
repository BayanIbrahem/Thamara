@file:OptIn(ExperimentalMaterial3Api::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.home.component


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashLazyRowTooltip
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.RankIconSize
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.UserRankIcon
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.dev_bayan_ibrahim.flashcards.ui.util.flashPlurals
import kotlinx.coroutines.launch

@Composable
fun HomeUser(
    modifier: Modifier = Modifier,
    user: User?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserHeader(user = user)
        user?.let {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserInfo(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.star,
                    label = stringResource(R.string.plays),
                    value = user.totalPlays.toString(),
                )
                UserInfo(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.rank,
                    label = stringResource(R.string.rank),
                    value = user.rank.toString(),
                )
            }
        }
    }
}

@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String,
    value: String,
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
                Text(value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun UserHeader(
    modifier: Modifier = Modifier,
    user: User?,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RankImage(rank = user?.rank?.rank)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                user?.name ?: stringResource(R.string.new_user),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = user?.age?.let {
                    flashPlurals(id = R.plurals.year, count = it)
                } ?: stringResource(R.string.enter_your_name_and_age),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun RankImage(
    modifier: Modifier = Modifier,
    rank: Int?
) {
    val coercedRank by remember (rank) {
        derivedStateOf {
            rank ?: UserRank.min_rank
        }
    }
    val state = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()
    FlashLazyRowTooltip(
        modifier = modifier,
        state = state,
        itemWidth = 48.dp,
        visibleItemsCount = 4,
        currentItemIndex = coercedRank,
        items = {
            items(UserRank.ranks_range.toList()) { r ->
                UserRankIcon(
                    modifier = modifier.alpha(if (r > coercedRank) 0.5f else 1f),
                    size = RankIconSize.MEDIUM,
                    rank = r,
                    current = r == (coercedRank)
                )
            }
        }
    ) {
        UserRankIcon(
            size = RankIconSize.BIG,
            rank = coercedRank,
            current = true,
            showText = false,
            onClickIcon = { scope.launch { state.show() } }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeUserPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.size(400.dp, 900.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            val user = User(name = "name", age = 10, rank = UserRank(10, 12))
            HomeUser(user = user)
        }
    }
}
