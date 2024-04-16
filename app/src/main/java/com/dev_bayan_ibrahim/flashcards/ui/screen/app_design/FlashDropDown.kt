package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dev_bayan_ibrahim.flashcards.data.util.FlashSelectableItem


@Composable
fun <T : FlashSelectableItem> FlashDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    items: List<T>,
    selectedItem: T,
    onSelectItem: (T) -> Unit,
    onDismiss: () -> Unit,
) {

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                leadingIcon = item.icon?.let { iconRes ->
                    {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = if (selectedItem == item) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null
                        )
                    }
                } else null,
                text = {
                    Text(text = stringResource(id = item.label))
                },
                onClick = { onSelectItem(item) }
            )
        }
    }
}