package ua.acclorite.book_story.presentation.screens.library.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.util.Selected
import ua.acclorite.book_story.presentation.core.components.CustomCoverImage
import ua.acclorite.book_story.presentation.core.util.calculateProgress

/**
 * Library list element item.
 *
 * @param book [Pair] of [Book] and [Selected] (Boolean).
 * @param modifier Modifier.
 * @param onCoverImageClick OnCoverImageClick callback.
 * @param onLongClick OnLongClick callback.
 * @param onButtonClick OnButtonClick callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryBookItem(
    book: Pair<Book, Selected>,
    modifier: Modifier = Modifier,
    onCoverImageClick: () -> Unit,
    onLongClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    val backgroundColor = remember(book.second) {
        if (book.second) primaryColor
        else Color.Transparent
    }
    val fontColor = remember(book.second) {
        if (book.second) onPrimaryColor
        else onSurfaceColor
    }

    val progress = rememberSaveable(book.first.progress) {
        "${book.first.progress.calculateProgress(1)}%"
    }

    Column(
        modifier
            .padding(3.dp)
            .background(
                backgroundColor,
                MaterialTheme.shapes.large
            )
            .padding(3.dp)
            .padding(bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f / 1.5f)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    MaterialTheme.shapes.medium
                )
                .combinedClickable(
                    onClick = {
                        onCoverImageClick()
                    },
                    onLongClick = {
                        onLongClick()
                    }
                )
        ) {
            if (book.first.coverImage != null) {
                CustomCoverImage(
                    uri = book.first.coverImage!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(
                        id = R.string.cover_image_not_found_content_desc
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f),
                    tint = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            }

            Text(
                progress,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(6.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )

            FilledIconButton(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(32.dp),
                shape = MaterialTheme.shapes.medium,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                        .copy(0.9f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        .copy(0.9f)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(id = R.string.continue_reading_content_desc),
                    Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            book.first.title,
            color = fontColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}