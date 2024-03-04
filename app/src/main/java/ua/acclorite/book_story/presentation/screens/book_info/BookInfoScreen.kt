package ua.acclorite.book_story.presentation.screens.book_info

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.Argument
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoBackground
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoChangeCoverBottomSheet
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoDeleteDialog
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoDescriptionSection
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoDetailsBottomSheet
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoInfoSection
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoMoreDropDown
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoMoveDialog
import ua.acclorite.book_story.presentation.screens.book_info.components.BookInfoStatisticSection
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.ui.DefaultTransition
import ua.acclorite.book_story.ui.Transitions
import ua.acclorite.book_story.ui.elevation
import ua.acclorite.book_story.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoScreen(
    libraryViewModel: LibraryViewModel = hiltViewModel(),
    browseViewModel: BrowseViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel(),
    navigator: Navigator
) {
    val context = LocalContext.current

    val viewModel by (context as ComponentActivity).viewModels<BookInfoViewModel>(
        extrasProducer = {
            val book = navigator.retrieveArgument("book") as? Book
            if (book == null) {
                navigator.navigateBack()
            }

            context.defaultViewModelCreationExtras
                .withCreationCallback<BookInfoViewModel.Factory> { factory ->
                    factory.create(book ?: Constants.EMPTY_BOOK)
                }
        }
    )

    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(Unit) {
        viewModel.init(
            navigator,
            failure = {
                Toast.makeText(
                    context,
                    context.getString(
                        R.string.file_not_found,
                        it.takeLast(40)
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    if (state.showChangeCoverBottomSheet) {
        BookInfoChangeCoverBottomSheet(
            libraryViewModel = libraryViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel
        )
    }
    if (state.showDetailsBottomSheet) {
        BookInfoDetailsBottomSheet(viewModel = viewModel)
    }
    if (state.showDeleteDialog) {
        BookInfoDeleteDialog(
            libraryViewModel = libraryViewModel,
            browseViewModel = browseViewModel,
            historyViewModel = historyViewModel,
            viewModel = viewModel,
            navigator = navigator
        )
    }
    if (state.showMoveDialog) {
        BookInfoMoveDialog(
            libraryViewModel = libraryViewModel,
            viewModel = viewModel,
            navigator = navigator
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedTopAppBar(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.elevation(),
                scrollBehavior = scrollBehavior,
                isTopBarScrolled = null,

                content1NavigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                content1Title = {
                    DefaultTransition(visible = firstVisibleItemIndex > 0) {
                        Text(
                            state.book.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                },
                content1Actions = {
                    Box {
                        DefaultTransition(
                            visible = !state.editTitle
                        ) {
                            BookInfoMoreDropDown(viewModel = viewModel)
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = state.editTitle,
                            enter = Transitions.DefaultTransitionIn,
                            exit = fadeOut(tween(200))
                        ) {
                            IconButton(
                                enabled = state.titleValue.isNotBlank() &&
                                        state.titleValue.trim() != state.book.title.trim(),
                                onClick = {
                                    viewModel.onEvent(BookInfoEvent.OnUpdateTitle(
                                        refreshList = {
                                            libraryViewModel.onEvent(
                                                LibraryEvent.OnLoadList
                                            )
                                        }
                                    ))
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.title_changed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Apply changes",
                                    tint =
                                    if (state.titleValue.isNotBlank()
                                        && state.titleValue != state.book.title
                                    )
                                        MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (state.book.coverImage != null) {
                            BookInfoBackground(
                                height = paddingValues.calculateTopPadding() + 12.dp + 195.dp,
                                image = state.book.coverImage!!.asImageBitmap()
                            )
                        }

                        Column(Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                            // Info
                            BookInfoInfoSection(viewModel = viewModel, book = state.book)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Statistic
                    BookInfoStatisticSection(book = state.book)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Description
                    BookInfoDescriptionSection(book = state.book)
                }
            }

            if (state.book.file != null) {
                FloatingActionButton(
                    onClick = {
                        navigator.navigate(
                            Screen.READER, Argument(
                                "book", state.book
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = FloatingActionButtonDefaults.elevation(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(13.dp))
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Continue reading",
                                Modifier.size(24.dp)
                            )
                            AnimatedVisibility(
                                visible = !listState.canScrollBackward
                            ) {
                                Text(
                                    text = stringResource(
                                        if (state.book.progress == 0f) R.string.start
                                        else R.string.continue_read
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                )
            }
        }
    }
}