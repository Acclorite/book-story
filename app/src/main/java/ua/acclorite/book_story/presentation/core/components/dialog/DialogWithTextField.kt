package ua.acclorite.book_story.presentation.core.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun DialogWithTextField(
    initialValue: String,
    lengthLimit: Int = Int.MAX_VALUE,
    onDismiss: () -> Unit,
    onAction: (String) -> UIText?
) {
    val state = remember(initialValue) {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(0, initialValue.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    val focused = remember { mutableStateOf(false) }

    val showError = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<UIText?>(null) }

    Dialog(
        title = stringResource(id = R.string.edit),
        description = null,
        onDismiss = { onDismiss() },
        actionEnabled = true,
        disableOnClick = false,
        onAction = {
            if (state.value.text.isBlank()) {
                onDismiss()
                return@Dialog
            }
            error.value = onAction(state.value.text)

            showError.value = error.value != null
            if (!showError.value) onDismiss()
        },
        withContent = true,
        items = {
            item {
                OutlinedTextField(
                    value = state.value,
                    onValueChange = {
                        if (it.text.length < lengthLimit || it.text.length < state.value.text.length) {
                            state.value = it
                            showError.value = false
                        }
                    },
                    isError = showError.value,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            if (!focused.value) {
                                focusRequester.requestFocus()
                                focused.value = true
                            }
                        },
                    colors = TextFieldDefaults.colors(
                        errorContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = initialValue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    supportingText = {
                        ExpandingTransition(
                            visible = showError.value && error.value != null
                        ) {
                            Text(
                                error.value!!.asString(),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                )
            }
        }
    )
}