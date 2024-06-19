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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomSelectionContainer
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.components.translator_language.TranslatorLanguageBottomSheet
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderEndItem
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.download_language_dialog.ReaderDownloadLanguageDialog
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.components.start_item.ReaderStartItem
import ua.acclorite.book_story.presentation.screens.reader.components.text.ReaderTextParagraph
import ua.acclorite.book_story.presentation.screens.reader.components.translator_bottom_sheet.ReaderTranslatorBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel

@Composable
fun ReaderScreenRoot(screen: Screen.Reader) {
    val navigator = LocalNavigator.current
    val context = LocalContext.current as ComponentActivity

    val viewModel: ReaderViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()
    val mainState = mainViewModel.state.collectAsState()

    val lazyListState = rememberSaveable(
        state.value.listState,
        saver = LazyListState.Saver
    ) {
        state.value.listState
    }
    val canScroll by remember {
        derivedStateOf {
            state.value.listState.canScrollBackward && state.value.listState.canScrollForward
        }
    }

    LaunchedEffect(Unit) {
        viewModel.init(
            screen = screen,
            onNavigate = { navigator.it() },
            context = context,
            refreshList = {
                libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                historyViewModel.onEvent(HistoryEvent.OnLoadList)
            },
            onError = {
                Toast.makeText(
                    context,
                    it.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
    LaunchedEffect(canScroll) {
        if (!canScroll && !state.value.showMenu && !state.value.loading) {
            viewModel.onEvent(ReaderEvent.OnShowHideMenu(context = context))
        }
    }
    LaunchedEffect(lazyListState) {
        viewModel.onUpdateProgress(
            onLibraryEvent = libraryViewModel::onEvent,
            onHistoryEvent = historyViewModel::onEvent
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearViewModel()
        }
    }

    ReaderScreen(
        state = state,
        mainState = mainState,
        lazyListState = lazyListState,
        onNavigate = { navigator.it() },
        onEvent = viewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent,
        onHistoryEvent = historyViewModel::onEvent
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ReaderScreen(
    state: State<ReaderState>,
    mainState: State<MainState>,
    lazyListState: LazyListState,
    onNavigate: OnNavigate,
    onEvent: (ReaderEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onHistoryEvent: (HistoryEvent) -> Unit,
) {
    val context = LocalContext.current as ComponentActivity
    val density = LocalDensity.current

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

    val text = remember(state.value.text) {
        state.value.text.toList().sortedBy { it.first }
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

    if (state.value.showTranslatorBottomSheet) {
        ReaderTranslatorBottomSheet(
            state = state,
            onEvent = onEvent
        )
    }
    if (state.value.showLanguageBottomSheet) {
        TranslatorLanguageBottomSheet(
            selectedLanguage = if (state.value.languageBottomSheetTranslateFrom) {
                state.value.book.translateFrom
            } else state.value.book.translateTo,

            unselectedLanguage = if (!state.value.languageBottomSheetTranslateFrom) {
                state.value.book.translateFrom
            } else state.value.book.translateTo,

            translateFromSelecting = state.value.languageBottomSheetTranslateFrom,
            onSelect = { from, to ->
                onEvent(
                    ReaderEvent.OnChangeTranslatorSettings(
                        translateFrom = from,
                        translateTo = to
                    )
                )
                onEvent(
                    ReaderEvent.OnShowHideLanguageBottomSheet(
                        show = false
                    )
                )
            },
            onDismiss = {
                onEvent(
                    ReaderEvent.OnShowHideLanguageBottomSheet(
                        show = false
                    )
                )
            }
        )
    }
    if (state.value.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet(
            mainState = mainState,
            onEvent = onEvent,
            onMainEvent = onMainEvent
        )
    }
    if (state.value.showDownloadLanguageDialog) {
        ReaderDownloadLanguageDialog(
            state = state,
            onEvent = onEvent
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
                    onNavigate = onNavigate,
                    onEvent = onEvent,
                    onLibraryUpdateEvent = onLibraryEvent,
                    onHistoryUpdateEvent = onHistoryEvent
                )
            }
        },
        bottomBar = {
            CustomAnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = state.value.showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar(
                    state = state,
                    onEvent = onEvent,
                    onLibraryUpdateEvent = onLibraryEvent,
                    onHistoryUpdateEvent = onHistoryEvent
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
                    ReaderEvent.OnOpenTranslator(
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
        ) { toolbarHidden ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .then(
                        if (!state.value.loading && toolbarHidden) {
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
                    ),
                verticalArrangement = Arrangement.spacedBy(paragraphHeight)
            ) {
                item {
                    DisableSelection {
                        ReaderStartItem(state = state)
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                customItems(
                    text, key = { key -> key.first }
                ) { line ->
                    ReaderTextParagraph(
                        state = state,
                        id = line.first,
                        line = line.second,
                        context = context,
                        density = density,
                        fontFamily = fontFamily,
                        fontColor = fontColor,
                        lineHeight = lineHeight,
                        fontStyle = fontStyle,
                        fontSize = mainState.value.fontSize!!.sp,
                        sidePadding = sidePadding,
                        paragraphIndentation = mainState.value.paragraphIndentation!!,
                        toolbarHidden = toolbarHidden,
                        onEvent = onEvent
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(18.dp))

                    DisableSelection {
                        ReaderEndItem(
                            state = state,
                            onNavigate = onNavigate,
                            onEvent = onEvent,
                            onLibraryEvent = onLibraryEvent,
                            onHistoryUpdateEvent = onHistoryEvent,
                        )
                    }
                }
            }
        }
    }

    if (state.value.loading || state.value.errorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.value.loading) {
                IsError(
                    errorMessage = state.value.errorMessage!!.asString(),
                    icon = painterResource(id = R.drawable.error),
                    actionTitle = stringResource(id = R.string.go_back),
                    action = {
                        onLibraryEvent(LibraryEvent.OnLoadList)
                        onEvent(
                            ReaderEvent.OnGoBack(
                                context,
                                refreshList = {
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                },
                                navigate = {
                                    onNavigate {
                                        navigateBack()
                                    }
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
                refreshList = {
                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                },
                navigate = {
                    onNavigate {
                        navigateBack()
                    }
                }
            )
        )
    }
}