package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
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
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.components.CustomSelectionContainer
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderEndItem
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderStartItem
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.ui.elevation
import ua.acclorite.book_story.util.Constants

@OptIn(FlowPreview::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScreen(
    mainViewModel: MainViewModel,
    libraryViewModel: LibraryViewModel,
    historyViewModel: HistoryViewModel,
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
                historyViewModel.onEvent(HistoryEvent.OnLoadList)
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
        containerColor = MaterialTheme.colorScheme.surface,
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
        CustomSelectionContainer(
            onCopyRequested = {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.copied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onTranslateRequested = { textToTranslate ->
                viewModel.onEvent(
                    ReaderEvent.OnTranslateText(
                        textToTranslate,
                        context,
                        noAppsFound = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_no_translator),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
            },
            onDictionaryRequested = { textToDefine ->
                viewModel.onEvent(
                    ReaderEvent.OnOpenDictionary(
                        textToDefine,
                        context,
                        noAppsFound = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_no_browser),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
            }
        ) { toolbarShowed ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .then(
                        if (!loading && toolbarShowed) {
                            Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        viewModel.onEvent(
                                            ReaderEvent.OnShowHideMenu(context = context)
                                        )
                                    }
                                )
                        } else {
                            Modifier
                                .fillMaxSize()
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
                    Column(
                        Modifier
                            .background(Color(backgroundColor.toULong()))
                            .fillMaxWidth()
                            .padding(
                                top = if (index == 0) 36.dp else 0.dp,
                                start = 18.dp,
                                end = 18.dp,
                                bottom = if (index == state.book.text.lastIndex) 36.dp
                                else (paragraphHeight * 3).dp
                            )
                    ) {
                        Text(
                            text = "${if (paragraphIndentation) "  " else ""}${text.line}",
                            color = Color(fontColor.toULong()),

                            style = TextStyle(
                                lineBreak = LineBreak.Paragraph
                            ),
                            fontFamily = fontFamily.font,
                            fontStyle = fontStyle,
                            fontSize = fontSize.sp,
                            lineHeight = (fontSize + lineHeight).sp
                        )
                    }
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
                        libraryViewModel.onEvent(LibraryEvent.OnLoadList)
                        historyViewModel.onEvent(HistoryEvent.OnLoadList)
                        viewModel.onEvent(ReaderEvent.OnShowSystemBars(context))
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