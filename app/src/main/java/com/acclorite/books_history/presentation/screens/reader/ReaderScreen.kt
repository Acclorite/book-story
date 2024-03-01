package com.acclorite.books_history.presentation.screens.reader

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.components.IsError
import com.acclorite.books_history.presentation.data.MainViewModel
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel
import com.acclorite.books_history.presentation.screens.reader.components.ReaderBottomBar
import com.acclorite.books_history.presentation.screens.reader.components.ReaderEndItem
import com.acclorite.books_history.presentation.screens.reader.components.ReaderSettingsBottomSheet
import com.acclorite.books_history.presentation.screens.reader.components.ReaderStartItem
import com.acclorite.books_history.presentation.screens.reader.components.ReaderTopBar
import com.acclorite.books_history.presentation.screens.reader.data.ReaderEvent
import com.acclorite.books_history.presentation.screens.reader.data.ReaderViewModel
import com.acclorite.books_history.ui.elevation
import com.acclorite.books_history.util.Constants
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScreen(
    mainViewModel: MainViewModel,
    navigator: Navigator
) {
    val context = LocalContext.current as ComponentActivity
    val systemBarsColor = MaterialTheme.elevation(20.dp).copy(0.85f)

    val viewModel by context.viewModels<ReaderViewModel>(
        extrasProducer = {
            val book = navigator.retrieveArgument("book") as? Book
            if (book == null) {
                navigator.navigateBack()
            }

            context.defaultViewModelCreationExtras
                .withCreationCallback<ReaderViewModel.Factory> { factory ->
                    factory.create(book ?: Constants.EMPTY_BOOK)
                }
        }
    )
    val libraryViewModel: LibraryViewModel = hiltViewModel()

    var loading by remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val velocity = consumed.y

                if ((velocity > 70 || velocity < -70) && state.showMenu) {
                    viewModel.onEvent(ReaderEvent.OnShowHideMenu(context = context))
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    val fontFamily = Constants.FONTS.find {
        it.id == mainViewModel.fontFamily.collectAsState().value!!
    } ?: Constants.FONTS[0]
    val fontStyle = when (mainViewModel.isItalic.collectAsState().value!!) {
        false -> FontStyle.Normal
        true -> FontStyle.Italic
    }
    val fontSize = mainViewModel.fontSize.collectAsState().value!!
    val lineHeight = mainViewModel.lineHeight.collectAsState().value!!
    val paragraphHeight = mainViewModel.paragraphHeight.collectAsState().value!!
    val paragraphIndentation = mainViewModel.paragraphIndentation.collectAsState().value!!
    val backgroundColor = mainViewModel.backgroundColor.collectAsState().value!!
    val fontColor = mainViewModel.fontColor.collectAsState().value!!

    LaunchedEffect(Unit) {
        viewModel.init(
            navigator,
            context,
            listState,
            refreshList = {
                libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
            },
            onLoaded = {
                loading = false
            }
        )
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(50).collectLatest {
            if (!loading) {
                val progress = if (it > 0) {
                    if ((it + listState.layoutInfo.visibleItemsInfo.size) >= state.book.text.lastIndex) {
                        1f
                    } else {
                        (it.toFloat() / (state.book.text.size - 1).toFloat())
                    }
                } else {
                    0f
                }

                viewModel.onEvent(
                    ReaderEvent.OnChangeProgress(
                        progress,
                        navigator,
                        refreshList = { book ->
                            libraryViewModel.onEvent(
                                LibraryEvent.OnUpdateBook(
                                    book
                                )
                            )
                        }
                    )
                )
            }
        }
    }

    if (state.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet(viewModel = viewModel, mainViewModel = mainViewModel)
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        containerColor = Color(backgroundColor.toULong()),
        topBar = {
            AnimatedVisibility(
                visible = state.showMenu,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                ReaderTopBar(
                    viewModel = viewModel,
                    navigator = navigator,
                    containerColor = systemBarsColor
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = state.showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar(
                    viewModel = viewModel,
                    libraryViewModel = libraryViewModel,
                    navigator = navigator,
                    scrollState = listState,
                    systemBarsColor = systemBarsColor
                )
            }
        }
    ) {
        SelectionContainer {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = !loading,
                        onClick = {
                            viewModel.onEvent(
                                ReaderEvent.OnShowHideMenu(context = context)
                            )
                        }
                    )
            ) {
                if (state.book.text.isNotEmpty()) {
                    item {
                        DisableSelection {
                            ReaderStartItem(viewModel = viewModel)
                        }
                    }
                }

                itemsIndexed(
                    state.book.text, key = { _, key -> key.id }
                ) { index, text ->
                    if (index == 0)
                        Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "${if (paragraphIndentation) "  " else ""}${text.line}",
                        color = Color(fontColor.toULong()),

                        style = TextStyle(
                            lineBreak = LineBreak.Paragraph
                        ),
                        fontFamily = fontFamily.font,
                        fontStyle = fontStyle,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize + lineHeight).sp,

                        modifier = Modifier.padding(horizontal = 18.dp)
                    )

                    if (index == state.book.text.lastIndex)
                        Spacer(modifier = Modifier.height(36.dp))
                    else
                        Spacer(modifier = Modifier.height((paragraphHeight * 3).dp))
                }

                if (state.book.text.isNotEmpty()) {
                    item {
                        DisableSelection {
                            ReaderEndItem(
                                libraryViewModel = libraryViewModel,
                                viewModel = viewModel,
                                navigator = navigator
                            )
                        }
                    }
                }
            }
        }
    }

    if (loading || state.errorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!loading && state.errorMessage != null) {
                IsError(
                    errorMessage = state.errorMessage!!.asString(),
                    icon = painterResource(id = R.drawable.error),
                    actionTitle = stringResource(id = R.string.go_back),
                    action = {
                        navigator.navigateBack()
                    }
                )
            } else {
                Text(
                    text = stringResource(id = R.string.loading),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.55f),
                    strokeCap = StrokeCap.Round,
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.elevation(2.dp)
                )
            }
        }
    }

    BackHandler {
        navigator.navigateBack()
        viewModel.onEvent(ReaderEvent.OnShowSystemBars(context))
    }
}