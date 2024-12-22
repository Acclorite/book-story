package ua.acclorite.book_story.presentation.reader

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun LazyItemScope.ReaderLayoutParagraph(
    activity: ComponentActivity,
    showMenu: Boolean,
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
    doubleClickTranslation: Boolean,
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
                        if (doubleClickTranslation && toolbarHidden) {
                            Modifier.noRippleClickable(
                                onDoubleClick = {
                                    openTranslator(
                                        ReaderEvent.OnOpenTranslator(
                                            textToTranslate = line.text,
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