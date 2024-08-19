package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import ua.acclorite.book_story.presentation.components.customItemsIndexed
import ua.acclorite.book_story.presentation.components.is_messages.IsError
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
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderTextParagraph
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.readerFastColorPresetChange
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.components.start_item.ReaderStartItem
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsState
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel

@Composable
fun ReaderScreenRoot(screen: Screen.Reader) {
    val navigator = LocalNavigator.current
    val context = LocalContext.current as ComponentActivity

    val viewModel: ReaderViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()
    val mainState = mainViewModel.state.collectAsState()
    val settingsState = settingsViewModel.state.collectAsState()

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
        settingsState = settingsState,
        lazyListState = lazyListState,
        onNavigate = { navigator.it() },
        onEvent = viewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onSettingsEvent = settingsViewModel::onEvent,
        onLibraryEvent = libraryViewModel::onEvent,
        onHistoryEvent = historyViewModel::onEvent
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ReaderScreen(
    state: State<ReaderState>,
    mainState: State<MainState>,
    settingsState: State<SettingsState>,
    lazyListState: LazyListState,
    onNavigate: OnNavigate,
    onEvent: (ReaderEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onHistoryEvent: (HistoryEvent) -> Unit,
) {
    val context = LocalContext.current as ComponentActivity

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
    val backgroundColor = animateColorAsState(
        settingsState.value.selectedColorPreset.backgroundColor,
        label = ""
    )
    val fontColor = animateColorAsState(
        settingsState.value.selectedColorPreset.fontColor,
        label = ""
    )
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

    if (state.value.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet(
            mainState = mainState,
            settingsState = settingsState,
            onEvent = onEvent,
            onMainEvent = onMainEvent,
            onSettingsEvent = onSettingsEvent
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
            onShareRequested = { textToShare ->
                onEvent(
                    ReaderEvent.OnOpenShareApp(
                        textToShare = textToShare,
                        context,
                        noAppsFound = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_no_share_app),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
            },
            onWebSearchRequested = { textToSearch ->
                onEvent(
                    ReaderEvent.OnOpenWebBrowser(
                        textToSearch = textToSearch,
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
            },
            onTranslateRequested = { textToTranslate ->
                onEvent(
                    ReaderEvent.OnOpenTranslator(
                        textToTranslate = textToTranslate,
                        translateWholeParagraph = false,
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
                                context.getString(R.string.error_no_dictionary),
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
                    .background(backgroundColor.value)
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
                    )
                    .readerFastColorPresetChange(
                        fastColorPresetChangeEnabled = mainState.value.fastColorPresetChange!!,
                        isLoading = state.value.loading,
                        toolbarHidden = toolbarHidden,
                        onSettingsEvent = onSettingsEvent,
                        presetChanged = {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(
                                        R.string.color_preset_selected_query,
                                        it
                                            .asString(context)
                                            .trim()
                                    ),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
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

                customItemsIndexed(
                    state.value.text, key = { _, index -> index }
                ) { _, line ->
                    ReaderTextParagraph(
                        line = line,
                        context = context,
                        fontFamily = fontFamily,
                        fontColor = fontColor.value,
                        lineHeight = lineHeight,
                        fontStyle = fontStyle,
                        textAlignment = mainState.value.textAlignment!!,
                        fontSize = mainState.value.fontSize!!.sp,
                        sidePadding = sidePadding,
                        paragraphIndentation = mainState.value.paragraphIndentation!!,
                        doubleClickTranslationEnabled = mainState.value.doubleClickTranslation!!,
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