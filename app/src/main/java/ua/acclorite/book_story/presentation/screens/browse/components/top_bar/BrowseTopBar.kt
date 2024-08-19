package ua.acclorite.book_story.presentation.screens.browse.components.top_bar

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.State
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
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.components.MoreDropDown
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout

/**
 * Browse Top Bar.
 *
 * @param state [BrowseState].
 * @param mainState [MainState].
 * @param filteredFiles Filtered files.
 * @param onEvent [BrowseEvent] callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTopBar(
    state: State<BrowseState>,
    mainState: State<MainState>,
    filteredFiles: List<SelectableFile>,
    onEvent: (BrowseEvent) -> Unit
) {
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
            when (mainState.value.browseLayout!!) {
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
        isTopBarScrolled = state.value.hasSelectedItems ||
                isScrolled.value ||
                inNestedDirectory.value,

        content1Visibility = !state.value.hasSelectedItems && !state.value.showSearch,
        content1NavigationIcon = {
            CustomAnimatedVisibility(
                visible = inNestedDirectory.value,
                enter = expandHorizontally(tween(200))
                        + slideInHorizontally(tween(200))
                        + fadeIn(tween(250)),
                exit = shrinkHorizontally(tween(150))
                        + slideOutHorizontally(tween(150))
                        + fadeOut(tween(200))
            ) {
                CustomIconButton(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.go_back_content_desc,
                    disableOnClick = false,
                    enabled = inNestedDirectory.value
                ) {
                    onEvent(BrowseEvent.OnGoBackDirectory)
                }
            }
        },
        content1Title = {
            Crossfade(
                targetState = inNestedDirectory.value,
                animationSpec = tween(200),
                label = ""
            ) {
                if (it) {
                    Text(
                        selectedDirectoryName.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        stringResource(id = R.string.browse_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        content1Actions = {
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
                    if (mainState.value.browseIncludedFilterItems!!.isNotEmpty()) {
                        MaterialTheme.colorScheme.primary
                    } else LocalContentColor.current,
                    label = ""
                ).value,
                enabled = !state.value.showFilterBottomSheet
            ) {
                onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
            }
            MoreDropDown()
        },

        content2Visibility = state.value.hasSelectedItems,
        content2NavigationIcon = {
            CustomIconButton(
                icon = Icons.Default.Clear,
                contentDescription = R.string.clear_selected_items_content_desc,
                disableOnClick = true
            ) {
                onEvent(BrowseEvent.OnClearSelectedFiles)
            }
        },
        content2Title = {
            Text(
                stringResource(
                    id = R.string.selected_items_count_query,
                    state.value.selectedItemsCount.coerceAtLeast(1)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        content2Actions = {
            CustomIconButton(
                icon = Icons.Default.SelectAll,
                contentDescription = R.string.select_all_files_content_desc,
                disableOnClick = false,
            ) {
                onEvent(
                    BrowseEvent.OnSelectFiles(
                        includedFileFormats = mainState.value.browseIncludedFilterItems!!,
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
        },

        content3Visibility = state.value.showSearch && !state.value.hasSelectedItems,
        content3NavigationIcon = {
            CustomIconButton(
                icon = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = R.string.exit_search_content_desc,
                disableOnClick = true
            ) {
                onEvent(BrowseEvent.OnSearchShowHide)
            }
        },
        content3Title = {
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
        content3Actions = {
            MoreDropDown()
        },

        customContent = {
            BrowseTopBarDirectoryPath(
                state = state,
                mainState = mainState,
                onEvent = onEvent
            )
        }
    )
}