package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicTextFieldBox(
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Search,
    keyboardAction: KeyboardActionScope.() -> Unit = {},
    modifyFocusWithAction: Boolean = true,
    hideKeyboardOnAction: Boolean = true,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
) {
    val keyboardActions by remember {
        val onKeyboardAction: KeyboardActionScope.() -> Unit = {
            keyboardAction()
            if (modifyFocusWithAction) {
                when (imeAction) {
                    ImeAction.Default, ImeAction.Go,
                    ImeAction.Search, ImeAction.Send,
                    ImeAction.Done -> focusManager.clearFocus()

                    ImeAction.Previous -> focusManager.moveFocus(FocusDirection.Previous)

                    ImeAction.Next -> focusManager.moveFocus(FocusDirection.Next)
                }
            }
            if (hideKeyboardOnAction) {
                softwareKeyboardController?.hide()
            }
        }
        derivedStateOf {
            when (imeAction) {
                ImeAction.Default -> KeyboardActions(onDone = onKeyboardAction)
                ImeAction.None -> KeyboardActions()
                ImeAction.Go -> KeyboardActions(onGo = onKeyboardAction)
                ImeAction.Search -> KeyboardActions(onSearch = onKeyboardAction)
                ImeAction.Send -> KeyboardActions(onSend = onKeyboardAction)
                ImeAction.Previous -> KeyboardActions(onPrevious = onKeyboardAction)
                ImeAction.Next -> KeyboardActions(onNext = onKeyboardAction)
                ImeAction.Done -> KeyboardActions(onDone = onKeyboardAction)
                else -> KeyboardActions()
            }
        }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField2(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = style,
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
            ),
            keyboardActions = keyboardActions
        )
        if (value.isBlank()) {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = placeHolder,
                style = style,
            )
        }
    }
}