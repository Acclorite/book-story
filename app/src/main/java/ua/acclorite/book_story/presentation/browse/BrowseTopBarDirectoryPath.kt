package ua.acclorite.book_story.presentation.browse

import android.os.Environment
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.BrowseFilesStructure
import ua.acclorite.book_story.domain.browse.FileWithTitle
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.browse.BrowseEvent
import java.io.File

@Composable
fun BrowseTopBarDirectoryPath(
    selectedDirectory: File,
    hasSelectedItems: Boolean,
    hasSearched: Boolean,
    isError: Boolean,
    filesStructure: BrowseFilesStructure,
    changeDirectory: (BrowseEvent.OnChangeDirectory) -> Unit
) {
    val rootDirectory = Environment.getExternalStorageDirectory()

    val directories = remember(selectedDirectory) {
        val directories = mutableListOf<FileWithTitle>()

        if (selectedDirectory == rootDirectory) {
            return@remember listOf(
                FileWithTitle(
                    title = UIText.StringResource(R.string.internal_storage),
                    file = rootDirectory
                )
            )
        }

        directories.add(
            FileWithTitle(
                title = UIText.StringValue(selectedDirectory.name),
                file = selectedDirectory
            )
        )
        var currentDirectory = selectedDirectory.parentFile ?: return@remember emptyList()

        while (true) {
            if (currentDirectory == rootDirectory) {
                break
            }

            directories.add(
                FileWithTitle(
                    title = UIText.StringValue(currentDirectory.name),
                    file = currentDirectory
                )
            )
            currentDirectory = currentDirectory.parentFile ?: continue
        }

        directories.add(
            FileWithTitle(
                title = UIText.StringResource(R.string.internal_storage),
                file = rootDirectory
            )
        )

        directories.reversed()
    }
    val listState = rememberLazyListState(directories.lastIndex)

    LaunchedEffect(directories) {
        try {
            listState.animateScrollToItem(directories.lastIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    AnimatedVisibility(
        visible = !hasSelectedItems
                && !hasSearched
                && !isError
                && filesStructure == BrowseFilesStructure.DIRECTORIES,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            state = listState,
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(
                directories,
                key = { index, _ -> index }
            ) { index, directory ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Text(
                    text = directory.title.asString(),
                    color = animateColorAsState(
                        if (index == directories.lastIndex) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        label = ""
                    ).value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.noRippleClickable(
                        enabled = selectedDirectory != directory.file
                    ) {
                        changeDirectory(
                            BrowseEvent.OnChangeDirectory(
                                directory = directory.file,
                                savePreviousDirectory = false
                            )
                        )
                    }
                )

                if (index < directories.lastIndex) {
                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowRight,
                        contentDescription = stringResource(id = R.string.path_arrow_icon_content_desc),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}