package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.presentation.core.components.common.CircularCheckbox
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.theme.DefaultTransition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BrowseGridDirectoryItem(
    file: SelectableFile,
    hasSelectedFiles: Boolean,
    onFavoriteClick: () -> Unit
) {
    val lastModified = rememberSaveable {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(file.fileOrDirectory.lastModified()))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (file.isSelected) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = stringResource(id = R.string.directory_icon_content_desc),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            DefaultTransition(
                visible = hasSelectedFiles,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                CircularCheckbox(
                    selected = file.isSelected,
                    containerColor = MaterialTheme.colorScheme.surface,
                    size = 18.dp
                )
            }
            DefaultTransition(
                visible = !hasSelectedFiles,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            file.fileOrDirectory.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            lastModified,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}