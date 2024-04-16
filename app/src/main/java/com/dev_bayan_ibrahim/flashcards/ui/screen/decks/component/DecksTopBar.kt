package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.BasicTextFieldBox
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.util.LoadableContentList

@Composable
fun DecksTopBar(
    modifier: Modifier = Modifier,

    dialogState: DecksFilterUiState,
    dialogActions: DecksFilterDialogUiActions,

    query: String,
    dbInfo: DecksDatabaseInfo,
    allTags: LoadableContentList<String>,
    allCollections: LoadableContentList<String>,
    onQueryChange: (String) -> Unit,
    onSelectTab: (DecksTab) -> Unit,
    onSearch: () -> Unit,
    selected: DecksTab,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BarTitle(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            dialogState = dialogState,
            selectedTab = selected,
            dbInfo = dbInfo,
            dialogActions = dialogActions,
            allTags = allTags,
            allCollections = allCollections,
        )
        BarSearch(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch
        )
        BarTab(selected = selected, onSelectTab = onSelectTab)
    }
}

@Composable
private fun BarTitle(
    modifier: Modifier = Modifier,
    dbInfo: DecksDatabaseInfo,
    dialogState: DecksFilterUiState,
    selectedTab: DecksTab,
    allTags: LoadableContentList<String>,
    allCollections: LoadableContentList<String>,
    dialogActions: DecksFilterDialogUiActions,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = FlashNavRoutes.TopLevel.Decks.nameRes),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )

        IconButton(
            onClick = dialogActions::onShowDialog
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            DecksFilterDialog(
                state = dialogState,
                dbInfo = dbInfo,
                selectedTab = selectedTab,
                allTags = allTags,
                allCollections = allCollections,
                actions = dialogActions
            )
        }
    }
}

@Composable
private fun BarSearch(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Filled.Search,
            contentDescription = null
        )
        BasicTextFieldBox(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChange,
            imeAction = ImeAction.Search,
            keyboardAction = { onSearch() },
            placeHolder = stringResource(R.string.search_for_decks),
        )
    }
}

@Composable
private fun BarTab(
    modifier: Modifier = Modifier,
    selected: DecksTab,
    onSelectTab: (DecksTab) -> Unit,
) {
    TabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selected.ordinal,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        DecksTab.entries.forEach { tab ->
            Tab(
                selected = tab == selected,
                onClick = { onSelectTab(tab) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                text = {
                    Text(
                        text = stringResource(id = tab.nameRes),
                    )
                }
            )
        }
    }
}
