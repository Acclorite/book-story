package ua.acclorite.book_story.presentation.screens.reader.components

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.FontWithName
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.ReaderTextAlignment

/**
 * Reader Text Paragraph item.
 *
 * @param line Current line.
 * @param context Context.
 * @param fontFamily Line's font family.
 * @param fontColor Line's font color.
 * @param lineHeight Line's line height.
 * @param fontStyle Line's font style.
 * @param textAlignment Line's text alignment.
 * @param fontSize Line's font size.
 * @param sidePadding Line's side padding.
 * @param paragraphIndentation Whether Paragraph Indentation is enabled for this line.
 * @param doubleClickTranslationEnabled Whether Double Click Translation is enabled.
 * @param toolbarHidden Whether selection toolBar is hidden.
 * @param onEvent [ReaderEvent] callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ReaderTextParagraph(
    line: String,
    context: Context,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    textAlignment: ReaderTextAlignment,
    fontSize: TextUnit,
    sidePadding: Dp,
    paragraphIndentation: Boolean,
    doubleClickTranslationEnabled: Boolean,
    toolbarHidden: Boolean,
    onEvent: (ReaderEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .animateItem(fadeInSpec = null, fadeOutSpec = null)
            .fillMaxWidth()
            .padding(
                horizontal = sidePadding
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = when (textAlignment) {
            ReaderTextAlignment.START, ReaderTextAlignment.JUSTIFY -> Alignment.Start
            ReaderTextAlignment.CENTER -> Alignment.CenterHorizontally
            ReaderTextAlignment.END -> Alignment.End
        }
    ) {
        BasicText(
            text = buildAnnotatedString {
                if (paragraphIndentation) {
                    append("  ")
                }

                append(line)
            },
            modifier = Modifier.then(
                if (
                    doubleClickTranslationEnabled &&
                    toolbarHidden
                ) {
                    Modifier.combinedClickable(
                        interactionSource = null,
                        indication = null,
                        onDoubleClick = {
                            onEvent(
                                ReaderEvent.OnOpenTranslator(
                                    textToTranslate = line,
                                    translateWholeParagraph = true,
                                    context = context as ComponentActivity,
                                    noAppsFound = {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.error_no_translator),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            )
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
            style = TextStyle(
                fontFamily = fontFamily.font,
                textAlign = when (textAlignment) {
                    ReaderTextAlignment.START -> TextAlign.Start
                    ReaderTextAlignment.JUSTIFY -> TextAlign.Justify
                    ReaderTextAlignment.CENTER -> TextAlign.Center
                    ReaderTextAlignment.END -> TextAlign.End
                },
                fontStyle = fontStyle,
                fontSize = fontSize,
                lineHeight = lineHeight,
                color = fontColor,
                lineBreak = LineBreak.Paragraph
            )
        )
    }
}