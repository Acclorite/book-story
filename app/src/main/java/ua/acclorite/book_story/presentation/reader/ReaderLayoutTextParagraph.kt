package ua.acclorite.book_story.presentation.reader

import androidx.activity.ComponentActivity
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.reader.ReaderText.Text
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.presentation.core.components.common.HighlightedText
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun LazyItemScope.ReaderLayoutTextParagraph(
    paragraph: Text,
    activity: ComponentActivity,
    showMenu: Boolean,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    textAlignment: ReaderTextAlignment,
    horizontalAlignment: Alignment.Horizontal,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    sidePadding: Dp,
    paragraphIndentation: TextUnit,
    fullscreenMode: Boolean,
    doubleClickTranslation: Boolean,
    highlightedReading: Boolean,
    highlightedReadingThickness: FontWeight,
    toolbarHidden: Boolean,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit
) {
    Column(
        modifier = Modifier
            .animateItem(fadeInSpec = null, fadeOutSpec = null)
            .fillMaxWidth()
            .padding(horizontal = sidePadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = horizontalAlignment
    ) {
        if (highlightedReading) {
            HighlightedText(
                text = paragraph.line,
                highlightThickness = highlightedReadingThickness,
                modifier = Modifier.then(
                    if (doubleClickTranslation && toolbarHidden) {
                        Modifier.noRippleClickable(
                            onDoubleClick = {
                                openTranslator(
                                    ReaderEvent.OnOpenTranslator(
                                        textToTranslate = paragraph.line.text,
                                        translateWholeParagraph = true,
                                        activity = activity
                                    )
                                )
                            },
                            onClick = {
                                menuVisibility(
                                    ReaderEvent.OnMenuVisibility(
                                        show = !showMenu,
                                        fullscreenMode = fullscreenMode,
                                        saveCheckpoint = true,
                                        activity = activity
                                    )
                                )
                            }
                        )
                    } else Modifier
                ),
                style = TextStyle(
                    fontFamily = fontFamily.font,
                    fontSynthesis = FontSynthesis.All,
                    textAlign = textAlignment.textAlignment,
                    textIndent = TextIndent(firstLine = paragraphIndentation),
                    fontStyle = fontStyle,
                    letterSpacing = letterSpacing,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    color = fontColor,
                    lineBreak = LineBreak.Paragraph
                )
            )
        } else {
            BasicText(
                text = paragraph.line,
                modifier = Modifier.then(
                    if (doubleClickTranslation && toolbarHidden) {
                        Modifier.noRippleClickable(
                            onDoubleClick = {
                                openTranslator(
                                    ReaderEvent.OnOpenTranslator(
                                        textToTranslate = paragraph.line.text,
                                        translateWholeParagraph = true,
                                        activity = activity
                                    )
                                )
                            },
                            onClick = {
                                menuVisibility(
                                    ReaderEvent.OnMenuVisibility(
                                        show = !showMenu,
                                        fullscreenMode = fullscreenMode,
                                        saveCheckpoint = true,
                                        activity = activity
                                    )
                                )
                            }
                        )
                    } else Modifier
                ),
                style = TextStyle(
                    fontFamily = fontFamily.font,
                    textAlign = textAlignment.textAlignment,
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