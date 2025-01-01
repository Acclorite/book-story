package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter

@Composable
fun ReaderLayoutTextChapter(
    chapter: Chapter,
    fontColor: Color,
    sidePadding: Dp
) {
    Spacer(modifier = Modifier.height(22.dp))
    Text(
        text = chapter.title,
        style = MaterialTheme.typography.headlineMedium,
        color = fontColor,
        modifier = Modifier.padding(horizontal = sidePadding)
    )
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = fontColor.copy(0.4f))
    Spacer(modifier = Modifier.height(16.dp))
}