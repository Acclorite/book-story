package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.browse.SelectableFile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseGridItem(
    modifier: Modifier,
    file: SelectableFile,
    hasSelectedFiles: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (file.isSelected) MaterialTheme.colorScheme.secondaryContainer
                else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            )
            .padding(5.dp)
    ) {
        when {
            !file.isDirectory -> {
                BrowseGridFileItem(
                    file = file,
                    hasSelectedFiles = hasSelectedFiles
                )
            }

            file.isDirectory -> {
                BrowseGridDirectoryItem(
                    file = file,
                    hasSelectedFiles = hasSelectedFiles,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}