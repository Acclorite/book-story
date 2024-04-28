package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomSelectionContainer
import ua.acclorite.book_story.presentation.components.customItemsIndexed
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderEndItem
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.components.start_item.ReaderStartItem
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel

@Composable
fun ReaderScreenRoot() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current as ComponentActivity

    val viewModel: ReaderViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()

    val listState = rememberLazyListState()
    val loading = remember { mutableStateOf(true) }

    val state = viewModel.state.collectAsState()
    val mainState = mainViewModel.state.collectAsState()

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
                loading.value = false
            }
        )
    }

    ReaderScreen(
        state = state,
        mainState = mainState,
        listState = listState,
        loading = loading,
        navigator = navigator,
        onEvent = viewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent,
        onHistoryEvent = historyViewModel::onEvent
    )
}

@OptIn(FlowPreview::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ReaderScreen(
    state: State<ReaderState>,
    mainState: State<MainState>,
    listState: LazyListState,
    loading: State<Boolean>,
    navigator: Navigator,
    onEvent: (ReaderEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onHistoryEvent: (HistoryEvent) -> Unit,
) {
    val context = LocalContext.current as ComponentActivity
    val systemBarsColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.85f)

    val canScroll by remember {
        derivedStateOf {
            listState.canScrollBackward && listState.canScrollForward
        }
    }
    val nestedScrollConnection = remember(state) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val velocity = consumed.y

                if ((velocity > 70 || velocity < -70) && state.value.showMenu && !state.value.lockMenu) {
                    onEvent(ReaderEvent.OnShowHideMenu(context = context))
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    val fontFamily = remember(mainState.value.fontFamily) {
        Constants.FONTS.find {
            it.id == mainState.value.fontFamily
        } ?: Constants.FONTS[0]
    }
    val fontColor = remember(mainState.value.fontColor) {
        Color(mainState.value.fontColor!!.toULong())
    }
    val backgroundColor = remember(mainState.value.backgroundColor) {
        Color(mainState.value.backgroundColor!!.toULong())
    }
    val lineHeight =
        remember(mainState.value.fontSize, mainState.value.lineHeight) {
            (mainState.value.fontSize!! + mainState.value.lineHeight!!).sp
        }
    val sidePadding = remember(mainState.value.sidePadding) {
        (mainState.value.sidePadding!! * 3).dp
    }
    val paragraphHeight = remember(mainState.value.paragraphHeight) {
        (mainState.value.paragraphHeight!! * 3).dp
    }
    val fontStyle = remember(mainState.value.isItalic) {
        when (mainState.value.isItalic!!) {
            true -> FontStyle.Italic
            false -> FontStyle.Normal
        }
    }


    LaunchedEffect(canScroll) {
        if (!canScroll && !state.value.showMenu && !loading.value) {
            onEvent(ReaderEvent.OnShowHideMenu(context = context))
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.debounce(500).collectLatest { items ->
            if (!loading.value && state.value.text.isNotEmpty() && listState.layoutInfo.totalItemsCount > 0) {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.last().index

                val progress = if (items.first > 0) {
                    if (lastVisibleItemIndex >= (listState.layoutInfo.totalItemsCount - 1)) {
                        1f
                    } else {
                        (items.first.toFloat() / (state.value.text.lastIndex).toFloat())
                    }
                } else {
                    0f
                }

                onEvent(
                    ReaderEvent.OnChangeProgress(
                        progress = progress,
                        navigator = navigator,
                        firstVisibleItemIndex = items.first,
                        firstVisibleItemOffset = items.second,
                        refreshList = { book ->
                            onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                            onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                        }
                    )
                )
            }
        }
    }

    if (state.value.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet(
            mainState = mainState,
            onEvent = onEvent,
            onMainEvent = onMainEvent
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CustomAnimatedVisibility(
                visible = state.value.showMenu,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                ReaderTopBar(
                    state = state,
                    onEvent = onEvent,
                    onLibraryUpdateEvent = onLibraryEvent,
                    onHistoryUpdateEvent = onHistoryEvent,
                    listState = listState,
                    containerColor = systemBarsColor
                )
            }
        },
        bottomBar = {
            CustomAnimatedVisibility(
                visible = state.value.showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar(
                    state = state,
                    onEvent = onEvent,
                    onLibraryUpdateEvent = onLibraryEvent,
                    onHistoryUpdateEvent = onHistoryEvent,
                    listState = listState,
                    containerColor = systemBarsColor
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
                onEvent(
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
                onEvent(
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
                        if (!loading.value && toolbarShowed) {
                            Modifier
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        onEvent(
                                            ReaderEvent.OnShowHideMenu(context = context)
                                        )
                                    }
                                )
                        } else {
                            Modifier
                        }
                    )
            ) {
                if (state.value.text.isNotEmpty()) {
                    item {
                        DisableSelection {
                            ReaderStartItem(state = state)
                        }
                    }
                }

                customItemsIndexed(
                    state.value.text, key = { key -> key.id }
                ) { index, line ->
                    val text = remember(mainState.value.paragraphIndentation, line) {
                        "${if (mainState.value.paragraphIndentation!!) "  " else ""}${line.line}"
                    }

                    Column(
                        Modifier
                            .background(backgroundColor)
                            .fillMaxWidth()
                            .padding(
                                top = if (index == 0) 18.dp else 0.dp,
                                start = sidePadding,
                                end = sidePadding,
                                bottom = if (index == state.value.text.lastIndex) 18.dp
                                else paragraphHeight
                            )
                    ) {
                        Text(
                            text = text,
                            color = fontColor,
                            style = TextStyle(lineBreak = LineBreak.Paragraph),
                            fontFamily = fontFamily.font,
                            fontStyle = fontStyle,
                            fontSize = mainState.value.fontSize!!.sp,
                            lineHeight = lineHeight
                        )
                    }
                }

                if (state.value.text.isNotEmpty()) {
                    item {
                        DisableSelection {
                            ReaderEndItem(
                                state = state,
                                onEvent = onEvent,
                                onLibraryEvent = onLibraryEvent,
                                onHistoryUpdateEvent = onHistoryEvent,
                                listState = listState
                            )
                        }
                    }
                }
            }
        }
    }

    if (loading.value || state.value.errorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!loading.value) {
                IsError(
                    errorMessage = state.value.errorMessage!!.asString(),
                    icon = painterResource(id = R.drawable.error),
                    actionTitle = stringResource(id = R.string.go_back),
                    action = {
                        onLibraryEvent(LibraryEvent.OnLoadList)
                        onEvent(
                            ReaderEvent.OnGoBack(
                                context,
                                navigator,
                                refreshList = {
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
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
        onEvent(
            ReaderEvent.OnGoBack(
                context,
                navigator,
                refreshList = {
                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                },
                listState = listState,
                navigate = {
                    it.navigateBack()
                }
            )
        )
    }
}