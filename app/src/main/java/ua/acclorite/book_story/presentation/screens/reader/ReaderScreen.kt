package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Constants
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

@OptIn(FlowPreview::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    mainViewModel: MainViewModel,
    libraryViewModel: LibraryViewModel,
    historyViewModel: HistoryViewModel,
    navigator: Navigator
) {
    val context = LocalContext.current as ComponentActivity
    val systemBarsColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.85f)

    var loading by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    val state by viewModel.state.collectAsState()
    val canScroll by remember {
        derivedStateOf {
            listState.canScrollBackward && listState.canScrollForward
        }
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val velocity = consumed.y

                if ((velocity > 70 || velocity < -70) && state.showMenu && !state.lockMenu) {
                    viewModel.onEvent(ReaderEvent.OnShowHideMenu(context = context))
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    val mainState by mainViewModel.state.collectAsState()
    val fontFamily = remember(mainState.fontFamily) {
        Constants.FONTS.find {
            it.id == mainState.fontFamily
        } ?: Constants.FONTS[0]
    }

    LaunchedEffect(Unit) {
        viewModel.init(
            navigator,
            context,
            refreshList = {
                libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                historyViewModel.onEvent(HistoryEvent.OnLoadList)
            },
            listState = listState,
            onLoaded = {
                loading = false
            }
        )
    }
    LaunchedEffect(canScroll) {
        if (!canScroll && !state.showMenu && !loading) {
            viewModel.onEvent(ReaderEvent.OnShowHideMenu(context = context))
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.debounce(500).collectLatest { items ->
            if (!loading && state.book.text.isNotEmpty() && listState.layoutInfo.totalItemsCount > 0) {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.last().index

                val progress = if (items.first > 0) {
                    if (lastVisibleItemIndex >= (listState.layoutInfo.totalItemsCount - 1)) {
                        1f
                    } else {
                        (items.first.toFloat() / (state.book.text.lastIndex).toFloat())
                    }
                } else {
                    0f
                }

                viewModel.onEvent(
                    ReaderEvent.OnChangeProgress(
                        progress = progress,
                        navigator = navigator,
                        firstVisibleItemIndex = items.first,
                        firstVisibleItemOffset = items.second,
                        refreshList = { book ->
                            libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(book))
                            historyViewModel.onEvent(HistoryEvent.OnUpdateBook(book))
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
                    libraryViewModel = libraryViewModel,
                    historyViewModel = historyViewModel,
                    navigator = navigator,
                    listState = listState,
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
                    historyViewModel = historyViewModel,
                    listState = listState,
                    navigator = navigator,
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
                    .fillMaxSize()
                    .then(
                        if (!loading && toolbarShowed) {
                            Modifier
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
                ) { index, line ->
                    val text = remember(mainState.paragraphIndentation) {
                        "${if (mainState.paragraphIndentation!!) "  " else ""}${line.line}"
                    }
                    val fontColor = remember(mainState.fontColor) {
                        Color(mainState.fontColor!!.toULong())
                    }
                    val backgroundColor = remember(mainState.backgroundColor) {
                        Color(mainState.backgroundColor!!.toULong())
                    }
                    val lineHeight = remember(mainState.fontSize, mainState.lineHeight) {
                        (mainState.fontSize!! + mainState.lineHeight!!).sp
                    }

                    Column(
                        Modifier
                            .background(backgroundColor)
                            .fillMaxWidth()
                            .padding(
                                top = if (index == 0) 18.dp else 0.dp,
                                start = 18.dp,
                                end = 18.dp,
                                bottom = if (index == state.book.text.lastIndex) 18.dp
                                else (mainState.paragraphHeight!! * 3).dp
                            )
                    ) {
                        Text(
                            text = text,
                            color = fontColor,
                            style = TextStyle(
                                lineBreak = LineBreak.Paragraph
                            ),
                            fontFamily = fontFamily.font,
                            fontStyle = when (mainState.isItalic!!) {
                                true -> FontStyle.Italic
                                false -> FontStyle.Normal
                            },
                            fontSize = mainState.fontSize!!.sp,
                            lineHeight = lineHeight
                        )
                    }
                }

                if (state.book.text.isNotEmpty()) {
                    item {
                        DisableSelection {
                            ReaderEndItem(
                                libraryViewModel = libraryViewModel,
                                historyViewModel = historyViewModel,
                                viewModel = viewModel,
                                listState = listState,
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
                        libraryViewModel.onEvent(LibraryEvent.OnLoadList)
                        viewModel.onEvent(
                            ReaderEvent.OnGoBack(
                                context,
                                navigator,
                                refreshList = {
                                    libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                                    historyViewModel.onEvent(HistoryEvent.OnUpdateBook(it))
                                },
                                listState = listState,
                                navigate = {
                                    it.navigateBack()
                                }
                            )
                        )
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
                    trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
                )
            }
        }
    }

    BackHandler {
        viewModel.onEvent(
            ReaderEvent.OnGoBack(
                context,
                navigator,
                refreshList = {
                    libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                    historyViewModel.onEvent(HistoryEvent.OnUpdateBook(it))
                },
                listState = listState,
                navigate = {
                    it.navigateBack()
                }
            )
        )
    }
}