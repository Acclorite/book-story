package ua.acclorite.book_story.presentation.screens.help.components.items

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState
import ua.acclorite.book_story.presentation.ui.SlidingTransition

@Composable
fun LazyItemScope.HelpFindBooksItem(
    state: State<HelpState>,
    onEvent: (HelpEvent) -> Unit
) {
    val context = LocalContext.current

    val sites = stringArrayResource(id = R.array.book_sites)
    val text = buildAnnotatedString {
        append(stringResource(id = R.string.help_field_support) + " ")

        sites.forEachIndexed { index, string ->
            HelpAnnotation(tag = string) {
                append(string.substringBeforeLast("|"))
            }

            if (index < sites.lastIndex) {
                append(", ")
            }
        }
    }

    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_find_books),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_find_books))
        },
        tags = emptyList(),
        shouldShowDescription = state.value.showHelpItem1,
        onTitleClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showHelpItem1 = !it.showHelpItem1
                    )
                }
            )
        },
        customContent = {
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = state.value.textFieldValue,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = { value ->
                    onEvent(
                        HelpEvent.OnUpdateState {
                            it.copy(
                                textFieldValue = value,
                                showError = false
                            )
                        }
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.help_field_placeholder))
                },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onEvent(
                            HelpEvent.OnSearchInWeb(
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
                        enabled = !state.value.showError,
                        color = if (state.value.showError) {
                            MaterialTheme.colorScheme.outline
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    ) {
                        onEvent(
                            HelpEvent.OnSearchInWeb(
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
                },
                supportingText = {
                    Column {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box {
                            SlidingTransition(visible = state.value.showError) {
                                Text(
                                    text = stringResource(id = R.string.help_field_error),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            SlidingTransition(visible = !state.value.showError) {
                                ClickableText(
                                    text = text,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) { offset ->
                                    sites.forEach { tag ->
                                        text.getStringAnnotations(
                                            tag = tag,
                                            start = offset,
                                            end = offset
                                        ).firstOrNull()?.let {
                                            onEvent(
                                                HelpEvent.OnNavigateToBrowserPage(
                                                    page = tag.substringAfterLast("|"),
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
                                    }

                                }
                            }
                        }
                    }
                },
                singleLine = true
            )
        },
        onTagClick = {}
    )
}