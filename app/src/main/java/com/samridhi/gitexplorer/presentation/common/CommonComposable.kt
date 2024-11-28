package com.samridhi.gitexplorer.presentation.common

import android.annotation.SuppressLint

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.samridhi.gitexplorer.presentation.ui.Grey200
import com.samridhi.gitexplorer.presentation.ui.labelColor
import com.samridhi.gitexplorer.presentation.ui.placeHolderColor
import com.samridhi.gitexplorer.presentation.ui.textColor


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldValue = TextFieldValue(),
    singleLine: Boolean = true,
    maxChar: Int = 100,
    onValueChange: (String) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeHolder: @Composable (() -> Unit) = {},
    allowSpecialCharacters: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var focused by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Column {
        Box(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = Grey200,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .bringIntoViewRequester(bringIntoViewRequester)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            TextField(
                singleLine = singleLine,
                value = textFieldState.text,
                onValueChange = {
                    if (it.length <= maxChar) {
                        val filteredText = if (!allowSpecialCharacters) {
                            if (KeyboardType.Number == keyboardType) {
                                it.filter { char -> char.isDigit() }
                            } else {
                                it.filter { char -> char.isLetter() }
                            }
                        } else {
                            it
                        }
                        onValueChange(filteredText.trim())
                    }
                },
                placeholder = placeHolder,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    disabledTextColor = textColor,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedPlaceholderColor = placeHolderColor,
                    unfocusedPlaceholderColor = placeHolderColor,
                    errorPlaceholderColor = placeHolderColor,
                    disabledLabelColor = labelColor,
                    focusedLabelColor = labelColor,
                    unfocusedLabelColor = labelColor,
                    cursorColor = textColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .defaultMinSize(56.dp)
                    .clickable(onClick = {})
                    .focusRequester(FocusRequester())
                    .onFocusChanged {
                        focused = it.isFocused
                    },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                textStyle = textStyle,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
    }
}