package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.CustomSelectionContainer
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.components.LocalSettingsViewModel
import ua.acclorite.book_story.presentation.core.components.customItemsIndexed
import ua.acclorite.book_story.presentation.core.components.is_messages.IsError
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderTextParagraph
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.readerFastColorPresetChange
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent

@Composable
fun ReaderScreenRoot(screen: Screen.Reader) {
    val state = LocalReaderViewModel.current.state
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val viewModel = LocalReaderViewModel.current.viewModel
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalOnNavigate.current


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
            onNavigate = onNavigate,
            context = context,
            refreshList = {
                onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                onHistoryEvent(HistoryEvent.OnLoadList)
            },
            onError = {
                it.asString(context)
                    .showToast(context = context)
            }
        )
    }
    LaunchedEffect(canScroll) {
        if (!canScroll && !state.value.showMenu && !state.value.loading) {
            viewModel.onEvent(ReaderEvent.OnShowHideMenu(context = context))
        }
    }
    LaunchedEffect(lazyListState) {
        viewModel.onUpdateProgress {
            onLibraryEvent(LibraryEvent.OnUpdateBook(it))
            onHistoryEvent(HistoryEvent.OnUpdateBook(it))
        }
    }

    ReaderScreen(lazyListState = lazyListState)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearViewModel()
            WindowCompat.getInsetsController(
                context.window,
                context.window.decorView
            ).show(WindowInsetsCompat.Type.systemBars())
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ReaderScreen(lazyListState: LazyListState) {
    val state = LocalReaderViewModel.current.state
    val mainState = LocalMainViewModel.current.state
    val settingsState = LocalSettingsViewModel.current.state
    val onEvent = LocalReaderViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalOnNavigate.current

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
    val lineHeight = remember(
        mainState.value.fontSize,
        mainState.value.lineHeight
    ) {
        (mainState.value.fontSize + mainState.value.lineHeight).sp
    }
    val letterSpacing = remember(mainState.value.letterSpacing) {
        (mainState.value.letterSpacing / 100f).em
    }
    val sidePadding = remember(mainState.value.sidePadding) {
        (mainState.value.sidePadding * 3).dp
    }
    val paragraphHeight = remember(mainState.value.paragraphHeight) {
        (mainState.value.paragraphHeight * 3).dp
    }
    val fontStyle = remember(mainState.value.isItalic) {
        when (mainState.value.isItalic) {
            true -> FontStyle.Italic
            false -> FontStyle.Normal
        }
    }

    if (state.value.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet()
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
                ReaderTopBar()
            }
        },
        bottomBar = {
            CustomAnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = state.value.showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar()
            }
        }
    ) {
        CustomSelectionContainer(
            onCopyRequested = {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    context.getString(R.string.copied)
                        .showToast(context = context, longToast = false)
                }
            },
            onShareRequested = { textToShare ->
                onEvent(
                    ReaderEvent.OnOpenShareApp(
                        textToShare = textToShare,
                        context,
                        noAppsFound = {
                            context.getString(R.string.error_no_share_app)
                                .showToast(context = context, longToast = false)
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
                            context.getString(R.string.error_no_browser)
                                .showToast(context = context, longToast = false)
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
                            context.getString(R.string.error_no_translator)
                                .showToast(context = context, longToast = false)
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
                            context.getString(R.string.error_no_dictionary)
                                .showToast(context = context, longToast = false)
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
                        fastColorPresetChangeEnabled = mainState.value.fastColorPresetChange,
                        isLoading = state.value.loading,
                        toolbarHidden = toolbarHidden,
                        presetChanged = {
                            context
                                .getString(
                                    R.string.color_preset_selected_query,
                                    it
                                        .asString(context)
                                        .trim()
                                )
                                .showToast(context = context, longToast = false)
                        }
                    )
                    .then(
                        if (mainState.value.cutoutPadding) Modifier.padding(
                            WindowInsets.displayCutout.asPaddingValues()
                        ) else Modifier
                    ),
                verticalArrangement = Arrangement.spacedBy(paragraphHeight),
                contentPadding = PaddingValues(
                    top = (WindowInsets.displayCutout.asPaddingValues()
                        .calculateTopPadding() + paragraphHeight)
                        .coerceAtLeast(18.dp),
                    bottom = (WindowInsets.displayCutout.asPaddingValues()
                        .calculateBottomPadding() + paragraphHeight)
                        .coerceAtLeast(18.dp),
                )
            ) {
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
                        textAlignment = mainState.value.textAlignment,
                        fontSize = mainState.value.fontSize.sp,
                        letterSpacing = letterSpacing,
                        sidePadding = sidePadding,
                        paragraphIndentation = mainState.value.paragraphIndentation,
                        doubleClickTranslationEnabled = mainState.value.doubleClickTranslation,
                        toolbarHidden = toolbarHidden
                    )
                }
            }
        }
    }

    if (state.value.loading || state.value.errorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
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
                                context = context,
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
                context = context,
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