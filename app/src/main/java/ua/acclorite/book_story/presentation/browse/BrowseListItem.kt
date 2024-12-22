package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.presentation.core.components.common.CircularCheckbox
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.theme.FadeTransitionPreservingSpace

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseListItem(
    modifier: Modifier,
    file: SelectableFile,
    hasSelectedFiles: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (file.isSelected) MaterialTheme.colorScheme.secondaryContainer
                else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .combinedClickable(
                onLongClick = {
                    onLongClick()
                },
                onClick = {
                    onClick()
                }
            )
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        when {
            !file.isDirectory -> {
                BrowseListFileItem(file = file)

                FadeTransitionPreservingSpace(visible = hasSelectedFiles) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(16.dp))

                        CircularCheckbox(
                            selected = file.isSelected,
                            containerColor = MaterialTheme.colorScheme.surface,
                            size = 18.dp
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            file.isDirectory -> {
                BrowseListDirectoryItem(file = file)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Box(contentAlignment = Alignment.Center) {
                        FadeTransitionPreservingSpace(hasSelectedFiles) {
                            CircularCheckbox(
                                selected = file.isSelected,
                                containerColor = MaterialTheme.colorScheme.surface,
                                size = 18.dp
                            )
                        }
                        FadeTransitionPreservingSpace(!hasSelectedFiles) {
                            Icon(
                                imageVector = if (file.isFavorite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = stringResource(
                                    id = R.string.favorite_directory_content_desc
                                ),
                                modifier = Modifier
                                    .size(24.dp)
                                    .noRippleClickable(enabled = !hasSelectedFiles) {
                                        onFavoriteClick()
                                    },
                                tint = if (file.isFavorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}