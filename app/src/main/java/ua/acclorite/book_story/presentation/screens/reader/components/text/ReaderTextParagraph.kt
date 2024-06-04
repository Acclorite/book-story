package ua.acclorite.book_story.presentation.screens.reader.components.text

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.FontWithName
import ua.acclorite.book_story.domain.model.LineWithTranslation
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import java.util.Locale

/**
 * Reader Text Paragraph item.
 *
 * @param state [ReaderState].
 * @param id Current paragraph [ID].
 * @param line Current line.
 * @param context Context.
 * @param density [Density] from LocalDensity.current.
 * @param fontFamily Line's font family.
 * @param fontColor Line's font color.
 * @param lineHeight Line's line height.
 * @param fontStyle Line's font style.
 * @param fontSize Line's font size.
 * @param sidePadding Line's side padding.
 * @param paragraphIndentation Whether Paragraph Indentation is enabled for this line.
 * @param onEvent [ReaderEvent] callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ReaderTextParagraph(
    state: State<ReaderState>,
    id: ID,
    line: LineWithTranslation,
    context: Context,
    density: Density,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    fontSize: TextUnit,
    sidePadding: Dp,
    paragraphIndentation: Boolean,
    onEvent: (ReaderEvent) -> Unit
) {
    val text = remember(
        paragraphIndentation,
        line.originalLine,
        line.translatedLine,
        line.useTranslation,
        line.translatingTo,
        line.translatingFrom
    ) {
        derivedStateOf {
            if (paragraphIndentation) "  "
            else "" +
                    if (line.useTranslation && line.translatedLine != null) line.translatedLine
                    else line.originalLine
        }
    }

    val translated = remember(line.useTranslation, line.translatedLine) {
        line.useTranslation && line.translatedLine != null
    }
    val showTrailingContent = remember(
        line.isTranslationLoading, line.isTranslationFailed
    ) {
        line.isTranslationLoading || line.isTranslationFailed
    }

    val translatingFrom = remember(line.translatingFrom) {
        val locale = Locale(line.translatingFrom)
        locale.getDisplayName(locale).replaceFirstChar { char ->
            char.uppercase()
        }
    }
    val translatingTo = remember(line.translatingTo) {
        val locale = Locale(line.translatingTo)
        locale.getDisplayName(locale).replaceFirstChar { char ->
            char.uppercase()
        }
    }

    Column(
        modifier = Modifier
            .animateItem(fadeInSpec = null, fadeOutSpec = null)
            .fillMaxWidth()
            .padding(
                horizontal = sidePadding
            ),
        verticalArrangement = Arrangement.Center
    ) {
        CustomAnimatedVisibility(
            modifier = Modifier.animateItem(
                placementSpec = tween(10),
                fadeInSpec = null,
                fadeOutSpec = null
            ),
            visible = translated,
            enter = fadeIn(tween(250)) + expandVertically(tween(250)),
            exit = fadeOut(tween(250)) + shrinkVertically(tween(250)),
        ) {
            DisableSelection {
                Text(
                    text = stringResource(
                        id = R.string.translator_show_original,
                        translatingFrom,
                        translatingTo
                    ),
                    color = fontColor,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(interactionSource = null, indication = null) {
                        onEvent(ReaderEvent.OnUndoTranslation(id))
                    }
                )
            }
        }

        BasicText(
            text = buildAnnotatedString {
                append(text.value)

                if (showTrailingContent) {
                    append(" ")
                    appendInlineContent("translator")
                }
            },
            modifier = Modifier
                .then(
                    if (
                        state.value.book.enableTranslator &&
                        state.value.book.doubleClickTranslation
                    ) {
                        Modifier.combinedClickable(
                            interactionSource = null,
                            indication = null,
                            onDoubleClick = {
                                if (line.isTranslationLoading) {
                                    return@combinedClickable
                                }

                                if (translated) {
                                    onEvent(ReaderEvent.OnUndoTranslation(id))
                                } else {
                                    onEvent(
                                        ReaderEvent.OnTranslateText(setOf(id)) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    it.asString(context),
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    )
                                }
                            },
                            onClick = {
                                onEvent(
                                    ReaderEvent.OnShowHideMenu(context = context as ComponentActivity)
                                )
                            }
                        )
                    } else {
                        Modifier
                    }
                ),
            inlineContent = readerTextTrailingContent(
                line = line,
                color = fontColor,
                textSize = fontSize,
                context = context,
                density = density
            ),
            style = TextStyle(
                fontFamily = fontFamily.font,
                fontStyle = fontStyle,
                fontSize = fontSize,
                lineHeight = lineHeight,
                color = fontColor,
                lineBreak = LineBreak.Paragraph
            )
        )
    }

}