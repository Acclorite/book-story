package ua.acclorite.book_story.presentation.screens.reader.components.translator_bottom_sheet

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import java.util.Locale

/**
 * Reader Translator Bottom Sheet. You can set up preferences for an individual book here and apply/cancel translation.
 *
 * @param state [ReaderState].
 * @param onEvent [ReaderEvent] callback.
 */
@Composable
fun ReaderTranslatorBottomSheet(
    state: State<ReaderState>,
    onEvent: (ReaderEvent) -> Unit
) {
    val context = LocalContext.current
    val book = remember(state.value) { state.value.book }

    val translateFrom = remember(book.translateFrom) {
        if (book.translateFrom == "auto") {
            return@remember context.getString(R.string.translator_auto_short)
        }

        val locale = Locale(book.translateFrom)
        locale.getDisplayName(locale).replaceFirstChar { char ->
            char.uppercase()
        }
    }
    val translateTo = remember(book.translateTo) {
        val locale = Locale(book.translateTo)
        locale.getDisplayName(locale).replaceFirstChar { char ->
            char.uppercase()
        }
    }

    val loading = remember { mutableStateOf(false) }
    val textTranslated = remember(state.value.isTranslating, state.value.text) {
        loading.value = true

        val hasTranslatedText = state.value.text.any {
            it.value.useTranslation && it.value.translatedLine != null
        }
        val loaded = state.value.text.none {
            it.value.isTranslationLoading
        }

        loading.value = false

        !state.value.isTranslating && hasTranslatedText && loaded
    }

    CustomBottomSheet(
        onDismissRequest = {
            onEvent(ReaderEvent.OnShowHideTranslatorBottomSheet())
        }
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            if (book.enableTranslator) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            if (book.enableTranslator) {
                                onEvent(ReaderEvent.OnCancelTranslation)
                            }

                            onEvent(
                                ReaderEvent.OnChangeTranslatorSettings(
                                    enableTranslator = !book.enableTranslator
                                )
                            )
                        }
                        .padding(horizontal = 15.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.translator_reader_settings),
                        color = if (book.enableTranslator) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.surface,
                        fontSize = 17.5.sp
                    )
                    Switch(
                        checked = book.enableTranslator,
                        onCheckedChange = null,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                ReaderTranslatorBottomSheetLanguageChooser(
                    fromLanguage = translateFrom,
                    toLanguage = translateTo,
                    onFromClick = {
                        onEvent(
                            ReaderEvent.OnShowHideLanguageBottomSheet(
                                show = true,
                                translateFrom = true
                            )
                        )
                    },
                    onToClick = {
                        onEvent(
                            ReaderEvent.OnShowHideLanguageBottomSheet(
                                show = true,
                                translateFrom = false
                            )
                        )
                    },
                    enabled = !state.value.isTranslating,
                    switchEnabled = book.translateFrom != "auto",
                    onSwitchClick = {
                        onEvent(
                            ReaderEvent.OnChangeTranslatorSettings(
                                translateFrom = book.translateTo,
                                translateTo = book.translateFrom
                            )
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
            }

            item {
                SwitchWithTitle(
                    selected = book.doubleClickTranslation,
                    title = stringResource(id = R.string.translator_double_click_to_translate_option),
                    description = stringResource(id = R.string.translator_double_click_to_translate_option_desc),
                    verticalPadding = 6.dp,
                    onClick = {
                        onEvent(
                            ReaderEvent.OnChangeTranslatorSettings(
                                doubleClickTranslation = !book.doubleClickTranslation
                            )
                        )
                    }
                )
            }

            item {
                SwitchWithTitle(
                    selected = book.translateWhenOpen,
                    title = stringResource(id = R.string.translator_translate_on_start_option),
                    description = stringResource(id = R.string.translator_translate_on_start_option_desc),
                    verticalPadding = 6.dp,
                    onClick = {
                        onEvent(
                            ReaderEvent.OnChangeTranslatorSettings(
                                translateWhenOpen = !book.translateWhenOpen
                            )
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(1f),
                        onClick = {
                            if (loading.value) {
                                return@Button
                            }

                            if (!state.value.isTranslating) {
                                if (!textTranslated) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.translator_starting),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onEvent(
                                        ReaderEvent.OnTranslateText(
                                            keys = null, /* means translating the whole text */
                                            error = {
                                                Toast.makeText(
                                                    context,
                                                    it.asString(context),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.translator_text_canceling),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onEvent(
                                        ReaderEvent.OnUndoTranslation(
                                            null /* means canceling the whole text translation */
                                        )
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.translator_canceling),
                                    Toast.LENGTH_SHORT
                                ).show()

                                loading.value = true
                                onEvent(
                                    ReaderEvent.OnCancelTranslation
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.value.isTranslating || textTranslated) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = contentColorFor(
                                if (state.value.isTranslating || textTranslated) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                        )
                    ) {
                        if (!state.value.isTranslating && !loading.value) {
                            Text(
                                text = if (textTranslated) stringResource(
                                    id = R.string.undo_translation
                                ) else stringResource(id = R.string.translate)
                            )
                        } else {
                            CircularProgressIndicator(
                                color = LocalContentColor.current,
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier.size(21.dp),
                                strokeWidth = 2.3.dp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(
                        8.dp + it
                    )
                )
            }
        }
    }
}