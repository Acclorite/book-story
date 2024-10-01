package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.FontWithName
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.ReaderTextAlignment

/**
 * Reader Text Paragraph item.
 *
 * @param line Current line.
 * @param fontFamily Line's font family.
 * @param fontColor Line's font color.
 * @param lineHeight Line's line height.
 * @param fontStyle Line's font style.
 * @param textAlignment Line's text alignment.
 * @param fontSize Line's font size.
 * @param letterSpacing Letter spacing.
 * @param sidePadding Line's side padding.
 * @param paragraphIndentation Paragraph Indentation for this line.
 * @param fullscreenMode Whether Reader is in fullscreen mode.
 * @param doubleClickTranslationEnabled Whether Double Click Translation is enabled.
 * @param toolbarHidden Whether selection toolBar is hidden.
 */
@Composable
fun LazyItemScope.ReaderTextParagraph(
    line: AnnotatedString,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    textAlignment: ReaderTextAlignment,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    sidePadding: Dp,
    paragraphIndentation: TextUnit,
    fullscreenMode: Boolean,
    doubleClickTranslationEnabled: Boolean,
    toolbarHidden: Boolean
) {
    val onEvent = LocalReaderViewModel.current.onEvent
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .animateItem(fadeInSpec = null, fadeOutSpec = null)
            .fillMaxWidth()
            .padding(horizontal = sidePadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = when (textAlignment) {
            ReaderTextAlignment.START, ReaderTextAlignment.JUSTIFY -> Alignment.Start
            ReaderTextAlignment.CENTER -> Alignment.CenterHorizontally
            ReaderTextAlignment.END -> Alignment.End
        }
    ) {
        when (line.text) {
            "---" -> {
                HorizontalDivider(
                    thickness = 3.dp,
                    modifier = Modifier.clip(CircleShape),
                    color = fontColor.copy(0.3f)
                )
            }

            else -> {
                BasicText(
                    text = line,
                    modifier = Modifier.then(
                        if (
                            doubleClickTranslationEnabled &&
                            toolbarHidden
                        ) {
                            Modifier.noRippleClickable(
                                onDoubleClick = {
                                    onEvent(
                                        ReaderEvent.OnOpenTranslator(
                                            textToTranslate = line.text,
                                            translateWholeParagraph = true,
                                            context = context as ComponentActivity,
                                            noAppsFound = {
                                                context.getString(R.string.error_no_translator)
                                                    .showToast(context = context, longToast = false)
                                            }
                                        )
                                    )
                                },
                                onClick = {
                                    onEvent(
                                        ReaderEvent.OnShowHideMenu(
                                            fullscreenMode = fullscreenMode,
                                            saveCheckpoint = true,
                                            activity = context as ComponentActivity
                                        )
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
                        textIndent = TextIndent(firstLine = paragraphIndentation),
                        fontStyle = fontStyle,
                        letterSpacing = letterSpacing,
                        fontSize = fontSize,
                        lineHeight = lineHeight,
                        color = fontColor,
                        lineBreak = LineBreak.Paragraph
                    )
                )
            }
        }
    }
}