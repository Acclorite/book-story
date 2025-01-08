@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.reader

import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.ui.reader.ReaderEvent

fun LazyListScope.ReaderLayoutText(
    activity: ComponentActivity,
    showMenu: Boolean,
    entry: ReaderText,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    chapterTitleAlignment: ReaderTextAlignment,
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
    when (entry) {
        is ReaderText.Image -> {
            item {
                ReaderLayoutTextImage(
                    entry = entry,
                    sidePadding = sidePadding
                )
            }
        }

        is ReaderText.Separator -> {
            item {
                ReaderLayoutTextSeparator(
                    sidePadding = sidePadding,
                    fontColor = fontColor
                )
            }
        }

        is ReaderText.Chapter -> {
            item {
                ReaderLayoutTextChapter(
                    chapter = entry,
                    chapterTitleAlignment = chapterTitleAlignment,
                    fontColor = fontColor,
                    sidePadding = sidePadding,
                    highlightedReading = highlightedReading,
                    highlightedReadingThickness = highlightedReadingThickness
                )
            }
        }

        is ReaderText.Text -> {
            item {
                ReaderLayoutTextParagraph(
                    paragraph = entry,
                    activity = activity,
                    showMenu = showMenu,
                    fontFamily = fontFamily,
                    fontColor = fontColor,
                    lineHeight = lineHeight,
                    fontStyle = fontStyle,
                    textAlignment = textAlignment,
                    horizontalAlignment = horizontalAlignment,
                    fontSize = fontSize,
                    letterSpacing = letterSpacing,
                    sidePadding = sidePadding,
                    paragraphIndentation = paragraphIndentation,
                    fullscreenMode = fullscreenMode,
                    doubleClickTranslation = doubleClickTranslation,
                    highlightedReading = highlightedReading,
                    highlightedReadingThickness = highlightedReadingThickness,
                    toolbarHidden = toolbarHidden,
                    openTranslator = openTranslator,
                    menuVisibility = menuVisibility
                )
            }
        }
    }
}