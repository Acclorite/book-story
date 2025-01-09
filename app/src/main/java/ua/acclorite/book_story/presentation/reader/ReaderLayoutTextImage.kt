package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.domain.reader.ReaderText

@Composable
fun LazyItemScope.ReaderLayoutTextImage(
    entry: ReaderText.Image,
    sidePadding: Dp,
    imagesCornersRoundness: Dp
) {
    Image(
        modifier = Modifier
            .animateItem(
                fadeInSpec = null,
                fadeOutSpec = null
            )
            .padding(horizontal = sidePadding)
            .clip(RoundedCornerShape(imagesCornersRoundness))
            .fillMaxWidth(),
        bitmap = entry.imageBitmap,
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}