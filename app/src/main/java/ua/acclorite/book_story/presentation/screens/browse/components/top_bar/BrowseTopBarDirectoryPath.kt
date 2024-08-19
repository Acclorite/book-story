package ua.acclorite.book_story.presentation.screens.browse.components.top_bar

import android.os.Environment
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.customItemsIndexed
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import java.io.File

@Composable
fun BrowseTopBarDirectoryPath(
    state: State<BrowseState>,
    mainState: State<MainState>,
    onEvent: (BrowseEvent) -> Unit
) {
    val context = LocalContext.current
    val rootDirectory = remember {
        Environment.getExternalStorageDirectory()
    }
    val directories = remember(state.value.selectedDirectory) {
        val directories = mutableListOf<Pair<String, File>>()

        if (state.value.selectedDirectory == rootDirectory) {
            return@remember listOf(
                context.getString(R.string.internal_storage) to rootDirectory
            )
        }

        directories.add(state.value.selectedDirectory.name to state.value.selectedDirectory)
        var currentDirectory = state.value.selectedDirectory.parentFile
            ?: return@remember emptyList()

        while (true) {
            if (currentDirectory == rootDirectory) {
                break
            }

            directories.add(
                currentDirectory.name to currentDirectory
            )
            currentDirectory = currentDirectory.parentFile ?: continue
        }

        directories.add(
            context.getString(R.string.internal_storage) to rootDirectory
        )

        directories.reversed()
    }
    val listState = rememberLazyListState(directories.lastIndex)

    LaunchedEffect(directories) {
        try {
            listState.scrollToItem(directories.lastIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    CustomAnimatedVisibility(
        visible = !state.value.hasSelectedItems
                && !state.value.hasSearched
                && !state.value.isError
                && mainState.value.browseFilesStructure == BrowseFilesStructure.DIRECTORIES,
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
            customItemsIndexed(directories, key = { _, index -> index }) { index, directory ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Text(
                    text = directory.first,
                    color = animateColorAsState(
                        if (index == directories.lastIndex) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        label = ""
                    ).value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(
                        enabled = state.value.selectedDirectory != directory.second,
                        indication = null,
                        interactionSource = null
                    ) {
                        onEvent(
                            BrowseEvent.OnChangeDirectory(
                                directory.second,
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