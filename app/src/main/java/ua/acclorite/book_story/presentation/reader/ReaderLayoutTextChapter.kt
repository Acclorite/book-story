package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter
import ua.acclorite.book_story.presentation.core.components.common.HighlightedText

@Composable
fun ReaderLayoutTextChapter(
    chapter: Chapter,
    fontColor: Color,
    sidePadding: Dp,
    highlightedReading: Boolean,
    highlightedReadingThickness: FontWeight
) {
    Spacer(modifier = Modifier.height(22.dp))
    if (highlightedReading) {
        HighlightedText(
            text = rememberSaveable(saver = AnnotatedString.Saver) {
                buildAnnotatedString { append(chapter.title) }
            },
            highlightThickness = highlightedReadingThickness,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = fontColor
            ),
            modifier = Modifier.padding(horizontal = sidePadding)
        )
    } else {
        Text(
            text = chapter.title,
            style = MaterialTheme.typography.headlineMedium,
            color = fontColor,
            modifier = Modifier.padding(horizontal = sidePadding)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = fontColor.copy(0.4f))
    Spacer(modifier = Modifier.height(16.dp))
}