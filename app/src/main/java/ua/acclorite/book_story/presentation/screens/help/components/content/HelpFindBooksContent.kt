package ua.acclorite.book_story.presentation.screens.help.components.content

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.ui.SlidingTransition

/**
 * Help Find Books Content.
 * [ua.acclorite.book_story.domain.model.HelpTip]'s custom content.
 */
@Composable
fun HelpFindBooksContent(onEvent: (HelpEvent) -> Unit) {
    val context = LocalContext.current
    val text = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }

    val sites = stringArrayResource(id = R.array.book_sites)
    val supportingText = buildAnnotatedString {
        append(stringResource(id = R.string.help_field_support) + " ")

        sites.forEachIndexed { index, string ->
            HelpAnnotation(
                onClick = {
                    onEvent(
                        HelpEvent.OnNavigateToBrowserPage(
                            page = string.substringAfterLast("|"),
                            context = context,
                            noAppsFound = {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_browser),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    )
                }
            ) {
                append(string.substringBeforeLast("|"))
            }

            if (index < sites.lastIndex) {
                append(", ")
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
        value = text.value,
        modifier = Modifier
            .fillMaxWidth(),
        onValueChange = { value ->
            text.value = value
            showError.value = false
        },
        label = {
            Text(text = stringResource(id = R.string.help_field_placeholder))
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onEvent(
                    HelpEvent.OnSearchInWeb(
                        page = text.value,
                        context = context,
                        error = {
                            showError.value = true
                        },
                        noAppsFound = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_no_browser),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
            }
        ),
        keyboardOptions = KeyboardOptions(
            KeyboardCapitalization.Words,
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            CustomIconButton(
                icon = Icons.Default.Search,
                contentDescription = R.string.search_content_desc,
                disableOnClick = false,
                enabled = !showError.value,
                color = when (showError.value) {
                    true -> MaterialTheme.colorScheme.outline
                    false -> MaterialTheme.colorScheme.primary
                },
                onClick = {
                    onEvent(
                        HelpEvent.OnSearchInWeb(
                            page = text.value,
                            context = context,
                            error = { showError.value = true },
                            noAppsFound = {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_browser),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    )
                }
            )
        },
        supportingText = {
            Column {
                Spacer(modifier = Modifier.height(2.dp))
                Box {
                    SlidingTransition(visible = showError.value) {
                        Text(
                            text = stringResource(id = R.string.help_field_error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    SlidingTransition(visible = !showError.value) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        },
        singleLine = true
    )
}