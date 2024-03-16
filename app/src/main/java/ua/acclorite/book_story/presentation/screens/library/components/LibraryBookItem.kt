package ua.acclorite.book_story.presentation.screens.library.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.data.removeDigits
import ua.acclorite.book_story.presentation.data.removeTrailingZero
import ua.acclorite.book_story.ui.elevation
import ua.acclorite.book_story.util.Selected

/**
 * Library list element item.
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
    val backgroundColor = if (book.second) MaterialTheme.colorScheme.primary
    else Color.Transparent
    val fontColor = if (book.second) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurface

    val progress = remember(book.first) {
        "${
            (book.first.progress * 100)
                .toDouble()
                .removeDigits(1)
                .removeTrailingZero()
        }%"
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        tween(300),
        label = "Background animation"
    )
    val animatedFontColor by animateColorAsState(
        targetValue = fontColor,
        tween(300),
        label = "Font color animation"
    )

    Column(
        modifier
            .padding(3.dp)
            .background(
                animatedBackgroundColor,
                MaterialTheme.shapes.large
            )
            .padding(3.dp)
            .padding(bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f / 1.5f)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.elevation())
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
                AsyncImage(
                    model = book.first.coverImage,
                    contentDescription = stringResource(id = R.string.cover_image_content_desc),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
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
                    tint = MaterialTheme.elevation(12.dp)
                )
            }

            Text(
                progress,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(6.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
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
                    contentDescription = "Continue reading",
                    Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            book.first.title,
            color = animatedFontColor,
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