package ua.acclorite.book_story.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.presentation.ui.elevation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * History list element item.
 */
@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    history: History,
    isOnClickEnabled: Boolean,
    onBodyClick: () -> Unit,
    onTitleClick: () -> Unit,
    isDeleteEnabled: Boolean,
    onDeleteClick: () -> Unit
) {
    val date = remember(history) { Date(history.time) }
    val pattern = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isOnClickEnabled) {
                onBodyClick()
            }
            .padding(top = 6.dp, bottom = 6.dp, start = 16.dp, end = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.89f)
        ) {
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.elevation())
            ) {
                if (history.book?.coverImage != null) {
                    AsyncImage(
                        model = history.book.coverImage,
                        contentDescription = stringResource(id = R.string.cover_image_content_desc),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
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
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    history.book?.title ?: return,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 21.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            enabled = isOnClickEnabled,
                            onClick = onTitleClick
                        ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    pattern.format(date),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(modifier = Modifier.weight(0.11f), contentAlignment = Alignment.CenterEnd) {
            IconButton(
                enabled = isDeleteEnabled && isOnClickEnabled,
                onClick = {
                    onDeleteClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete this history element",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}