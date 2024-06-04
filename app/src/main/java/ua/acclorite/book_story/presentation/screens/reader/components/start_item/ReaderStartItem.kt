package ua.acclorite.book_story.presentation.screens.reader.components.start_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomCoverImage
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState

/**
 * Reader start item. Displays at the beginning of the book.
 *
 * @param state [ReaderState].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReaderStartItem(state: State<ReaderState>) {
    val statusBarHeight = WindowInsets
        .statusBarsIgnoringVisibility
        .asPaddingValues()
        .calculateTopPadding()

    val itemsHeight = remember(statusBarHeight) {
        statusBarHeight + 80.dp + 140.dp + 40.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (state.value.book.coverImage != null) {
            ReaderStartItemBackground(
                height = itemsHeight,
                image = state.value.book.coverImage!!
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarHeight)
                .padding(horizontal = 24.dp, vertical = 80.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .width(100.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    if (state.value.book.coverImage != null) {
                        CustomCoverImage(
                            uri = state.value.book.coverImage!!,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = stringResource(id = R.string.cover_image_not_found_content_desc),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.7f)
                                .aspectRatio(1f),
                            tint = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        state.value.book.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        stringResource(id = R.string.happy_reading),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}