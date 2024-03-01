package com.acclorite.books_history.presentation.screens.library

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.presentation.Argument
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.presentation.components.AnimatedTopAppBar
import com.acclorite.books_history.presentation.components.IsEmpty
import com.acclorite.books_history.presentation.components.MoreDropDown
import com.acclorite.books_history.presentation.screens.library.components.LibraryBookItem
import com.acclorite.books_history.presentation.screens.library.components.LibraryDeleteDialog
import com.acclorite.books_history.presentation.screens.library.components.LibraryMoveDialog
import com.acclorite.books_history.presentation.screens.library.components.LibraryTabRow
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel
import com.acclorite.books_history.ui.Transitions
import com.acclorite.books_history.ui.elevation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, FlowPreview::class
)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    navigator: Navigator,
    addedBooks: List<Book>
) {
    val state by viewModel.state.collectAsState()
    val pagerState =
        rememberPagerState(initialPage = state.currentPage, pageCount = { Category.entries.size })
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            viewModel.onEvent(LibraryEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (addedBooks.isNotEmpty()) {
            viewModel.onEvent(LibraryEvent.OnPreloadBooks(addedBooks))
            navigator.clearArgument("added_books")
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(LibraryEvent.OnScrollToPage(state.currentPage, pagerState))
    }
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onEvent(LibraryEvent.OnUpdateCurrentPage(pagerState.currentPage))
    }

    if (state.showMoveDialog) {
        LibraryMoveDialog(viewModel = viewModel, pagerState = pagerState)
    }
    if (state.showDeleteDialog) {
        LibraryDeleteDialog(viewModel = viewModel)
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                AnimatedTopAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.elevation(),

                    scrollBehavior = null,
                    isTopBarScrolled = state.hasSelectedItems,

                    content1Visibility = !state.hasSelectedItems && !state.showSearch,
                    content1NavigationIcon = {},
                    content1Title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                stringResource(id = R.string.library_screen),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = state.books.size.toString(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.elevation(elevation = 6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    content1Actions = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.OnSearchShowHide) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search files",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        MoreDropDown(navigator = navigator)
                    },

                    content2Visibility = state.hasSelectedItems,
                    content2NavigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.OnClearSelectedBooks) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear selected items",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    content2Title = {
                        Text(
                            stringResource(
                                id = R.string.selected_items_count_query,
                                state.selectedItemsCount.coerceAtLeast(1)
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    content2Actions = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.OnShowHideMoveDialog) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.DriveFileMove,
                                contentDescription = "Move books to another category",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.OnShowHideDeleteDialog) }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete books from database",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },

                    content3Visibility = state.showSearch && !state.hasSelectedItems,
                    content3NavigationIcon = {
                        IconButton(onClick = {
                            viewModel.onEvent(
                                LibraryEvent.OnSearchShowHide
                            )
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Exit search mode",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    content3Title = {
                        BasicTextField(
                            value = state.searchQuery,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                            ),
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onGloballyPositioned {
                                    viewModel.onEvent(LibraryEvent.OnRequestFocus(focusRequester))
                                },
                            onValueChange = {
                                viewModel.onEvent(LibraryEvent.OnSearchQueryChange(it))
                            },
                            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Words),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
                        ) { innerText ->
                            Box(
                                modifier = Modifier.fillMaxHeight(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        stringResource(
                                            id = R.string.search_query,
                                            stringResource(id = R.string.books)
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.titleLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                innerText()
                            }
                        }
                    },
                    content3Actions = {
                        MoreDropDown(navigator = navigator)
                    }
                )
                LibraryTabRow(
                    viewModel = viewModel,
                    books = state.books.map { it.first },
                    pagerState = pagerState
                )
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            HorizontalPager(state = pagerState, userScrollEnabled = !state.isRefreshing) { index ->
                var categoryIsLoading by remember { mutableStateOf(true) }
                val categorizedBooks = remember { mutableStateListOf<Pair<Book, Boolean>>() }
                val category = Category.entries[index]

                LaunchedEffect(state.books) {
                    categorizedBooks.clear()
                    categorizedBooks.addAll(state.books.filter { it.first.category == category }
                        .sortedByDescending {
                            it.first.lastOpened
                        })
                    categoryIsLoading = false
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        if (!state.isLoading) {
                            items(
                                categorizedBooks,
                                key = { it.first.id ?: 0 }
                            ) {
                                LibraryBookItem(
                                    book = it,
                                    modifier = Modifier.animateItemPlacement(
                                        animationSpec = tween(300)
                                    ),
                                    onCoverImageClick = {
                                        if (state.hasSelectedItems) {
                                            viewModel.onEvent(LibraryEvent.OnSelectBook(it))
                                        } else {
                                            navigator.navigate(
                                                Screen.BOOK_INFO,
                                                Argument("book", it.first)
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        if (!it.second) {
                                            viewModel.onEvent(LibraryEvent.OnSelectBook(it, true))
                                        }
                                    },
                                    onButtonClick = {
                                        navigator.navigate(
                                            Screen.READER,
                                            Argument("book", it.first)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (state.isLoading && !state.isRefreshing) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = !state.isLoading && !state.isRefreshing && categorizedBooks.isEmpty()
                                && !categoryIsLoading,
                        modifier = Modifier.align(Alignment.Center),
                        enter = Transitions.DefaultTransitionIn,
                        exit = fadeOut(tween(0))
                    ) {
                        IsEmpty(
                            message = stringResource(id = R.string.library_empty),
                            icon = painterResource(id = R.drawable.empty_library),
                            modifier = Modifier.align(Alignment.Center),
                            actionTitle = stringResource(id = R.string.add_book)
                        ) {
                            navigator.navigate(Screen.BROWSE)
                        }
                    }

                    PullRefreshIndicator(
                        state.isRefreshing,
                        refreshState,
                        Modifier.align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.elevation(),
                        contentColor = MaterialTheme.colorScheme.primary,
                        scale = true
                    )
                }
            }
        }
    }

    val activity = LocalContext.current as ComponentActivity
    val scope = rememberCoroutineScope()
    var shouldExit = false
    BackHandler {
        if (state.hasSelectedItems) {
            viewModel.onEvent(LibraryEvent.OnClearSelectedBooks)
            return@BackHandler
        }

        if (state.showSearch) {
            viewModel.onEvent(LibraryEvent.OnSearchShowHide)
            return@BackHandler
        }

        if (state.currentPage > 0) {
            viewModel.onEvent(LibraryEvent.OnScrollToPage(0, pagerState))
            return@BackHandler
        }

        if (shouldExit) {
            activity.finish()
            return@BackHandler
        }

        Toast.makeText(
            activity,
            activity.getString(R.string.press_again_toast),
            Toast.LENGTH_SHORT
        ).show()
        shouldExit = true

        scope.launch {
            delay(1500)
            shouldExit = false
        }
    }
}