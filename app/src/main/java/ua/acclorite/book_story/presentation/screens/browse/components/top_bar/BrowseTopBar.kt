package ua.acclorite.book_story.presentation.screens.browse.components.top_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.core.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.core.components.AnimatedTopAppBarData
import ua.acclorite.book_story.presentation.core.components.CustomIconButton
import ua.acclorite.book_story.presentation.core.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.navigation.NavigationIconButton
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout

/**
 * Browse Top Bar.
 *
 * @param filteredFiles Filtered files.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTopBar(filteredFiles: List<SelectableFile>) {
    val state = LocalBrowseViewModel.current.state
    val mainState = LocalMainViewModel.current.state
    val onEvent = LocalBrowseViewModel.current.onEvent

    val focusRequester = remember { FocusRequester() }
    val selectedDirectoryName = remember {
        mutableStateOf(state.value.selectedDirectory.name)
    }
    val inNestedDirectory = remember {
        derivedStateOf {
            state.value.inNestedDirectory
                    && mainState.value.browseFilesStructure != BrowseFilesStructure.ALL_FILES
        }
    }
    val isScrolled = remember {
        derivedStateOf {
            when (mainState.value.browseLayout) {
                BrowseLayout.LIST -> state.value.listState.canScrollBackward
                BrowseLayout.GRID -> state.value.gridState.canScrollBackward
            }
        }
    }

    LaunchedEffect(
        state.value.inNestedDirectory,
        state.value.selectedDirectory
    ) {
        if (state.value.inNestedDirectory) {
            selectedDirectoryName.value = state.value.selectedDirectory.name
        }
    }

    AnimatedTopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = isScrolled.value || state.value.hasSelectedItems,

        shownTopBar = when {
            state.value.hasSelectedItems -> 3
            state.value.showSearch -> 2
            state.value.inNestedDirectory -> 1
            else -> 0
        },
        topBars = listOf(
            AnimatedTopAppBarData(
                contentID = 0,
                contentNavigationIcon = {},
                contentTitle = {
                    Text(
                        stringResource(id = R.string.browse_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                contentActions = {
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                    CustomIconButton(
                        icon = Icons.Default.FilterList,
                        contentDescription = R.string.filter_content_desc,
                        disableOnClick = false,
                        color = animateColorAsState(
                            if (mainState.value.browseIncludedFilterItems.isNotEmpty()) {
                                MaterialTheme.colorScheme.primary
                            } else LocalContentColor.current,
                            label = ""
                        ).value,
                        enabled = !state.value.showFilterBottomSheet
                    ) {
                        onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
                    }
                    NavigationIconButton()
                }
            ),

            AnimatedTopAppBarData(
                contentID = 1,
                contentNavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.go_back_content_desc,
                        disableOnClick = false,
                        enabled = inNestedDirectory.value
                    ) {
                        onEvent(BrowseEvent.OnGoBackDirectory)
                    }
                },
                contentTitle = {
                    Text(
                        selectedDirectoryName.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                contentActions = {
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                    CustomIconButton(
                        icon = Icons.Default.FilterList,
                        contentDescription = R.string.filter_content_desc,
                        disableOnClick = false,
                        color = animateColorAsState(
                            if (mainState.value.browseIncludedFilterItems.isNotEmpty()) {
                                MaterialTheme.colorScheme.primary
                            } else LocalContentColor.current,
                            label = ""
                        ).value,
                        enabled = !state.value.showFilterBottomSheet
                    ) {
                        onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
                    }
                    NavigationIconButton()
                }
            ),

            AnimatedTopAppBarData(
                contentID = 2,
                contentNavigationIcon = {
                    CustomIconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                },
                contentTitle = {
                    CustomSearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                onEvent(BrowseEvent.OnRequestFocus(focusRequester))
                            },
                        query = state.value.searchQuery,
                        onQueryChange = {
                            onEvent(BrowseEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            onEvent(BrowseEvent.OnSearch)
                        },
                        placeholder = stringResource(
                            id = R.string.search_query,
                            stringResource(id = R.string.files)
                        )
                    )
                },
                contentActions = {
                    NavigationIconButton()
                }
            ),

            AnimatedTopAppBarData(
                contentID = 3,
                contentNavigationIcon = {
                    CustomIconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = R.string.clear_selected_items_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnClearSelectedFiles)
                    }
                },
                contentTitle = {
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            state.value.selectedItemsCount.coerceAtLeast(1)
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                contentActions = {
                    CustomIconButton(
                        icon = Icons.Default.SelectAll,
                        contentDescription = R.string.select_all_files_content_desc,
                        disableOnClick = false,
                    ) {
                        onEvent(
                            BrowseEvent.OnSelectFiles(
                                includedFileFormats = mainState.value.browseIncludedFilterItems,
                                files = filteredFiles
                            )
                        )
                    }
                    CustomIconButton(
                        icon = Icons.Default.Check,
                        contentDescription = R.string.add_files_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.showAddingDialog
                    ) {
                        onEvent(BrowseEvent.OnAddingDialogRequest)
                    }
                }
            )
        ),
        customContent = {
            BrowseTopBarDirectoryPath()
        }
    )
}