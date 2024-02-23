package com.acclorite.books_history.presentation.screens.book_info

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.presentation.Argument
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoBackground
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoChangeCoverBottomSheet
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoDeleteDialog
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoDescriptionSection
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoDetailsBottomSheet
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoInfoSection
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoMoreDropDown
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoMoveDialog
import com.acclorite.books_history.presentation.screens.book_info.components.BookInfoStatisticSection
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoEvent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoViewModel
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel
import com.acclorite.books_history.ui.DefaultTransition
import com.acclorite.books_history.ui.Transitions
import com.acclorite.books_history.ui.elevation
import com.acclorite.books_history.util.Constants
import dagger.hilt.android.lifecycle.withCreationCallback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoScreen(
    libraryViewModel: LibraryViewModel = hiltViewModel(),
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
    val scrollState = rememberScrollState()

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
        BookInfoChangeCoverBottomSheet(libraryViewModel = libraryViewModel, viewModel = viewModel)
    }
    if (state.showDetailsBottomSheet) {
        BookInfoDetailsBottomSheet(viewModel = viewModel)
    }
    if (state.showDeleteDialog) {
        BookInfoDeleteDialog(
            libraryViewModel = libraryViewModel,
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
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
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
                title = {},
                actions = {
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.elevation()
                )
            )
        }
    ) { paddingValues ->
        if (state.book.coverImage != null) {
            BookInfoBackground(image = state.book.coverImage!!.asImageBitmap())
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.height(12.dp))
                // Info
                BookInfoInfoSection(viewModel = viewModel, book = state.book)

                Spacer(modifier = Modifier.height(24.dp))
                // Statistic
                BookInfoStatisticSection(book = state.book)

                Spacer(modifier = Modifier.height(24.dp))
                // Description
                BookInfoDescriptionSection(book = state.book)
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
                                visible = scrollState.value < 5
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