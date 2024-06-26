package ua.acclorite.book_story.presentation.screens.browse.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Selected
import ua.acclorite.book_story.presentation.components.CustomCheckbox
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Browse list element item. Can be selected.
 *
 * @param file [File].
 * @param hasSelectedFiles Whether parent list has selected items.
 * @param modifier Modifier.
 * @param onClick OnClick callback.
 * @param onLongClick OnLongClick callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseFileItem(
    file: Pair<File, Selected>,
    hasSelectedFiles: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val pattern = remember { SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()) }
    val lastModified = rememberSaveable { pattern.format(Date(file.first.lastModified())) }
    val sizeBytes = rememberSaveable { file.first.length() }

    val fileSizeKB = rememberSaveable { if (sizeBytes > 0) sizeBytes.toDouble() / 1024.0 else 0.0 }
    val fileSizeMB = rememberSaveable { if (sizeBytes > 0) fileSizeKB / 1024.0 else 0.0 }
    val fileSize = rememberSaveable {
        if (fileSizeMB >= 1.0) "%.2f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.2f KB".format(fileSizeKB)
        else "0 KB"
    }

    val outlineColor = if (file.second) MaterialTheme.colorScheme.outline
    else MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = if (file.second) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor, RoundedCornerShape(10.dp))
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.88f)
        ) {
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        outlineColor,
                        RoundedCornerShape(6.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                    contentDescription = stringResource(id = R.string.file_icon_content_desc),
                    modifier = Modifier
                        .size(22.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    file.first.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    lineHeight = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$fileSize, $lastModified",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(0.12f)
                .padding(end = 6.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            DefaultTransition(
                visible = hasSelectedFiles
            ) {
                CustomCheckbox(
                    selected = file.second,
                    containerColor = MaterialTheme.colorScheme.surface,
                    size = 18.dp
                )
            }
        }
    }
}