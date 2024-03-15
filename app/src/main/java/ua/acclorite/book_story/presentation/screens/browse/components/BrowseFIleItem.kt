package ua.acclorite.book_story.presentation.screens.browse.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomCheckbox
import ua.acclorite.book_story.ui.DefaultTransition
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Browse list element item. Can be selected.
 */
@Composable
fun BrowseFileItem(
    file: Pair<File, Boolean>,
    hasSelectedFiles: Boolean,
    modifier: Modifier, onClick: () -> Unit
) {
    val pattern = remember { SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()) }
    val lastModified = remember { pattern.format(Date(file.first.lastModified())) }
    val sizeBytes = remember { file.first.length() }

    val fileSizeKB = remember { if (sizeBytes > 0) sizeBytes.toDouble() / 1024.0 else 0.0 }
    val fileSizeMB = remember { if (sizeBytes > 0) fileSizeKB / 1024.0 else 0.0 }
    val fileSize = remember {
        if (fileSizeMB >= 1.0) "%.2f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.2f KB".format(fileSizeKB)
        else "0 KB"
    }

    val outlineColor = if (file.second) MaterialTheme.colorScheme.outline
    else MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = if (file.second) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent

    val animatedOutlineColor by animateColorAsState(
        targetValue = outlineColor,
        tween(300),
        label = stringResource(id = R.string.outline_anim_content_desc)
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        tween(300),
        label = stringResource(id = R.string.background_anim_content_desc)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(animatedBackgroundColor)
            .clickable {
                onClick()
            }
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
                        animatedOutlineColor,
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
                CustomCheckbox(selected = file.second, size = 18.dp)
            }
        }
    }
}