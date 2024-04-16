package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.data.util.FlashSelectableItem
import com.dev_bayan_ibrahim.flashcards.ui.app.util.getThrowableMessage
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDropDown
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashRangeSlider
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toFloatRange
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksFilterTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.dev_bayan_ibrahim.flashcards.ui.util.LoadableContentList
import com.dev_bayan_ibrahim.flashcards.ui.util.MutableLoadableContentList
import io.ktor.util.Hash
import kotlinx.collections.immutable.PersistentSet
import kotlinx.coroutines.launch

@Stable
interface DecksFilterUiState {
    val show: Boolean
    val groupType: DecksGroupType?
    val orderType: DecksOrderType?
    val ascOrder: Boolean
    val filter: DecksFilter

    fun getValuesKey(query: String): Int {
        val g = groupType?.ordinal?.inc() ?: 0
        val o = orderType?.ordinal?.inc() ?: 0
        val a = if (ascOrder) 1 else 0
        return Hash.combine(g, o, a, query, filter.hashCode())
    }
}

class DecksFilterMutableUiState : DecksFilterUiState {
    fun onApply(appliedFilters: DecksFilterMutableUiState) {
        groupType = appliedFilters.groupType
        orderType = appliedFilters.orderType
        filter = appliedFilters.filter
    }

    fun reset() {
        onApply(DecksFilterMutableUiState())
    }

    override var show: Boolean by mutableStateOf(false)
    override var groupType: DecksGroupType? by mutableStateOf(null)
    override var orderType: DecksOrderType? by mutableStateOf(null)
    override var ascOrder: Boolean by mutableStateOf(true)
    override var filter: DecksFilter by mutableStateOf(DecksFilter())
}

@Immutable
interface DecksFilterDialogUiActions {
    fun onSelectGroupType(groupType: DecksGroupType)
    fun onSelectOrderType(orderType: DecksOrderType)
    fun onSelectTag(tag: String)
    fun onDeselectAllTags()
    fun onSelectCollection(collection: String)
    fun onDeselectAllCollections()
    fun onApply()
    fun onCancel()
    fun onReset()
    fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>)
    fun onRateValueChange(rates: ClosedFloatingPointRange<Float>)
    fun onShowDialog()
    fun changeOrderType(asc: Boolean)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DecksFilterDialog(
    modifier: Modifier = Modifier,
    state: DecksFilterUiState,
    dbInfo: DecksDatabaseInfo,
    selectedTab: DecksTab,
    allTags: LoadableContentList<String>,
    allCollections: LoadableContentList<String>,
    actions: DecksFilterDialogUiActions

) {
    FlashDialog(
        modifier = modifier
            .height(590.dp)
            .aspectRatio(ratio = cardRatio),
        show = state.show,
        onDismiss = actions::onCancel
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val pagerState = rememberPagerState {
                DecksFilterTab.entries.count()
            }
            val scope = rememberCoroutineScope()
            FilterTabs(
                selectedTab = DecksFilterTab.entries[pagerState.currentPage]
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(it.ordinal)
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = 32.dp,
                        top = 8.dp,
                        end = 32.dp,
                        bottom = 24.dp
                    ),
            ) {
                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    state = pagerState,
                    userScrollEnabled = false,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (DecksFilterTab.entries[page]) {
                        DecksFilterTab.Display -> DisplayPage(
                            modifier = Modifier.fillMaxWidth(),
                            state = state,
                            actions = actions
                        )

                        DecksFilterTab.Tag -> {
                            LocalOrRemoteItems(
                                selectedTab = selectedTab,
                                label = stringResource(id = R.string.tags),
                                dbItems = dbInfo.tags,
                                allItems = allTags,
                                selectedItems = state.filter.tags,
                                onSelectItem = actions::onSelectTag,
                                onDeselectAll = actions::onDeselectAllTags
                            )
                        }

                        DecksFilterTab.Collection -> {
                            LocalOrRemoteItems(
                                selectedTab = selectedTab,
                                label = stringResource(id = R.string.collections),
                                dbItems = dbInfo.collections,
                                allItems = allCollections,
                                selectedItems = state.filter.collections,
                                onSelectItem = actions::onSelectCollection,
                                onDeselectAll = actions::onDeselectAllCollections
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = actions::onReset) {
                        Text(stringResource(R.string.reset))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = actions::onCancel) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = actions::onApply) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }

        }
    }
}

@Composable
private fun LocalOrRemoteItems(
    modifier: Modifier = Modifier,
    label: String,
    selectedTab: DecksTab,
    dbItems: Collection<String>,
    allItems: LoadableContentList<String>,
    selectedItems: PersistentSet<String>,
    onSelectItem: (String) -> Unit,
    onDeselectAll: () -> Unit,
) {
    when (selectedTab) {
        DecksTab.LIBRARY -> {
            FilterMultipleStringItems(
                modifier = modifier.fillMaxWidth(),
                label = label,
                allItems = dbItems,
                selectedItems = selectedItems,
                onSelectItem = onSelectItem,
                onDeselectAll = onDeselectAll,
            )
        }

        DecksTab.BROWSE -> {
            if (allItems.loading) {
                Box(
                    modifier = modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = modifier
                            .size(40.dp)
                    )
                }
            } else {
                allItems.error?.let {
                    Text(
                        text = getThrowableMessage(throwable = it),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } ?: FilterMultipleStringItems(
                    modifier = modifier.fillMaxWidth(),
                    label = label,
                    allItems = allItems.content,
                    selectedItems = selectedItems,
                    onSelectItem = onSelectItem,
                    onDeselectAll = onDeselectAll,
                )
            }
        }
    }
}

@Composable
private fun FilterTabs(
    modifier: Modifier = Modifier,
    selectedTab: DecksFilterTab,
    onSelectTab: (DecksFilterTab) -> Unit,
) {
    TabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTab.ordinal,
        containerColor = Color.Transparent
    ) {
        DecksFilterTab.entries.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onSelectTab(tab) },
                text = {
                    Text(text = stringResource(id = tab.nameRes))
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = tab.icon),
                        contentDescription = null,
                    )
                }
            )
        }
    }
}

@Composable
private fun DisplayPage(
    modifier: Modifier = Modifier,
    state: DecksFilterUiState,
    actions: DecksFilterDialogUiActions,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        FilterSection(title = stringResource(R.string.group_by)) {
            FilterDialogItem(
                items = DecksGroupType.entries,
                selectedItem = state.groupType ?: DecksGroupType.NONE,
                onSelectItem = actions::onSelectGroupType,
            )
        }
        FilterSection(title = stringResource(R.string.order_by)) {
            FilterDialogItem(
                items = DecksOrderType.entries,
                selectedItem = state.orderType ?: DecksOrderType.NAME,
                onSelectItem = actions::onSelectOrderType,
            )
        }
        Row(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(24.dp))
                .padding(8.dp)
                .clickable {
                    actions.changeOrderType(!state.ascOrder)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = state.ascOrder,
                onCheckedChange = null
            )
            Text(stringResource(R.string.asc_order))
        }

        FilterRangeSlider(
            label = stringResource(R.string.levels),
            validRange = 0f..10f,
            selectedRange = state.filter.levels?.toFloatRange(),
            onValueChange = actions::onLevelsValueChange
        )

        FilterRangeSlider(
            label = stringResource(id = R.string.rate),
            validRange = 0f..5f,
            selectedRange = state.filter.rate,
            onValueChange = actions::onRateValueChange
        )
    }
}

@Composable
private fun FilterSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun <T : FlashSelectableItem> FilterDialogItem(
    modifier: Modifier = Modifier,
    direction: LayoutDirection = LocalLayoutDirection.current,
    items: List<T>,
    selectedItem: T,
    onSelectItem: (T) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 0f else when (direction) {
            LayoutDirection.Ltr -> 1f
            LayoutDirection.Rtl -> -1f
        } * -90f,
        label = ""
    )
    Box(
        modifier = modifier,
    ) {
        AssistChip(
            onClick = { expanded = true },
            label = {
                Text(text = stringResource(id = selectedItem.label))
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotation
                    },
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        FlashDropDown(
            expanded = expanded,
            items = items,
            selectedItem = selectedItem,
            onSelectItem = {
                onSelectItem(it)
                expanded = false
            },
            onDismiss = { expanded = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterMultipleStringItems(
    modifier: Modifier = Modifier,
    label: String,
    allItems: Collection<String>,
    selectedItems: PersistentSet<String>,
    onSelectItem: (String) -> Unit,
    onDeselectAll: () -> Unit,
) {
    if (allItems.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label)
                TextButton(onClick = onDeselectAll) {
                    Text(text = stringResource(R.string.deselect))
                }
            }
            FlowRow(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                allItems.forEach { item ->
                    FilterChip(
                        selected = item in selectedItems,
                        onClick = { onSelectItem(item) },
                        label = {
                            Text(text = item)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRangeSlider(
    modifier: Modifier = Modifier,
    label: String,
    validRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    selectedRange: ClosedFloatingPointRange<Float>?,
) {
    Column(
        modifier = Modifier,
    ) {
        Text(label)
        FlashRangeSlider(
            modifier = modifier,
            steps = validRange.run { endInclusive - start - 1 }.toInt(),
            onValueChange = onValueChange,
            selectedRange = selectedRange ?: validRange,
            valueRange = validRange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DecksFilterDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            DecksFilterDialog(
                state = DecksFilterMutableUiState(),
                dbInfo = DecksDatabaseInfo(setOf("tag 1", "tag 2")),
                selectedTab = DecksTab.LIBRARY,
                allTags = MutableLoadableContentList(),
                allCollections = MutableLoadableContentList(),
                actions = object : DecksFilterDialogUiActions {
                    override fun onSelectGroupType(groupType: DecksGroupType) {}
                    override fun onSelectOrderType(orderType: DecksOrderType) {}
                    override fun onSelectTag(tag: String) {}
                    override fun onDeselectAllTags() {}
                    override fun onSelectCollection(collection: String) {}
                    override fun onDeselectAllCollections() {}
                    override fun onApply() {}
                    override fun onCancel() {}
                    override fun onReset() {}
                    override fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>) {}
                    override fun onRateValueChange(rates: ClosedFloatingPointRange<Float>) {}
                    override fun onShowDialog() {}
                    override fun changeOrderType(asc: Boolean) {}
                }
            )
        }
    }
}
