package com.external.charts.common.components.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Bottom sheet to show items in vertical list view with a close button at top
 */
@ExperimentalMaterialApi
@Composable
fun AccessibilityBottomSheetDialog(
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    popUpTopRightButtonTitle: String,
    popUpTopRightButtonDescription: String,
    sheetState: ModalBottomSheetState
) {
    val composeScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = modifier
                    .background(backgroundColor)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            composeScope.launch { sheetState.hide() }
                        }) {
                            Text(
                                text = popUpTopRightButtonTitle,
                                modifier = Modifier.semantics {
                                    contentDescription = popUpTopRightButtonDescription
                                })
                        }
                    }
                    content()
                }
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.White,
        sheetElevation = 0.dp
    ) {}
}
