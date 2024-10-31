package ua.acclorite.book_story.presentation.screens.reader

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.common.SelectionContainer
import ua.acclorite.book_story.presentation.core.components.placeholder.ErrorPlaceholder
import ua.acclorite.book_story.presentation.core.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideFonts
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.core.util.setBrightness
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderChapter
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderChaptersDrawer
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderPerceptionExpander
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderTextParagraph
import ua.acclorite.book_story.presentation.screens.reader.components.ReaderUpdateDialog
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderBottomBar
import ua.acclorite.book_story.presentation.screens.reader.components.app_bar.ReaderTopBar
import ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet.ReaderSettingsBottomSheet
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.ReaderTextAlignment

@Composable
fun ReaderScreenRoot(screen: Screen.Reader) {
    val state = ReaderViewModel.getState()
    val mainState = MainViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalNavigator.current


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
        onEvent(
            ReaderEvent.OnInit(
                screen = screen,
                navigateBack = {
                    onNavigate {
                        navigateBack()
                    }
                },
                fullscreenMode = mainState.value.fullscreen,
                checkForTextUpdate = mainState.value.checkForTextUpdate,
                checkForTextUpdateToast = {
                    if (mainState.value.checkForTextUpdateToast) {
                        context.getString(R.string.nothing_changed).showToast(
                            context = context,
                            longToast = false
                        )
                    }
                },
                activity = context,
                refreshList = {
                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                    onHistoryEvent(HistoryEvent.OnLoadList)
                },
                onError = {
                    it.asString(context)
                        .showToast(context = context)
                }
            )
        )
    }
    LaunchedEffect(canScroll) {
        if (!canScroll && !state.value.showMenu && !state.value.loading) {
            onEvent(
                ReaderEvent.OnShowHideMenu(
                    fullscreenMode = mainState.value.fullscreen,
                    saveCheckpoint = true,
                    activity = context
                )
            )
        }
    }
    LaunchedEffect(mainState.value.fullscreen) {
        onEvent(
            ReaderEvent.OnShowHideMenu(
                show = state.value.showMenu,
                fullscreenMode = mainState.value.fullscreen,
                saveCheckpoint = false,
                activity = context
            )
        )
    }
    LaunchedEffect(lazyListState) {
        onEvent(
            ReaderEvent.OnUpdateProgress(
                refreshList = {
                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                }
            )
        )
    }
    DisposableEffect(mainState.value.screenOrientation) {
        context.requestedOrientation = mainState.value.screenOrientation.code
        onDispose {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    DisposableEffect(
        mainState.value.screenBrightness,
        mainState.value.customScreenBrightness
    ) {
        when (mainState.value.customScreenBrightness) {
            true -> context.setBrightness(brightness = mainState.value.screenBrightness)
            false -> context.setBrightness(brightness = null)
        }

        onDispose {
            context.setBrightness(brightness = null)
        }
    }
    DisposableEffect(mainState.value.keepScreenOn) {
        when (mainState.value.keepScreenOn) {
            true -> context.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    ReaderScreen(lazyListState = lazyListState)

    DisposableEffect(Unit) {
        onDispose {
            onEvent(ReaderEvent.OnClearViewModel)
            WindowCompat.getInsetsController(
                context.window,
                context.window.decorView
            ).show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ReaderScreen(lazyListState: LazyListState) {
    val state = ReaderViewModel.getState()
    val mainState = MainViewModel.getState()
    val settingsState = SettingsViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalNavigator.current

    val nestedScrollConnection = remember(state) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                consumed.y.let { velocity ->
                    if (velocity in -70f..70f) return@let
                    if (!state.value.showMenu) return@let
                    if (state.value.lockMenu) return@let
                    if (!mainState.value.hideBarsOnFastScroll) return@let

                    onEvent(
                        ReaderEvent.OnShowHideMenu(
                            show = false,
                            fullscreenMode = mainState.value.fullscreen,
                            saveCheckpoint = false,
                            activity = context
                        )
                    )
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    val fontFamily = remember(mainState.value.fontFamily) {
        Constants.provideFonts(withRandom = true).find {
            it.id == mainState.value.fontFamily
        } ?: Constants.provideFonts(withRandom = false)[0]
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
    val verticalPadding = remember(mainState.value.verticalPadding) {
        (mainState.value.verticalPadding * 4.5f).dp
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
    val paragraphIndentation = remember(
        mainState.value.paragraphIndentation,
        mainState.value.textAlignment
    ) {
        if (
            mainState.value.textAlignment == ReaderTextAlignment.CENTER ||
            mainState.value.textAlignment == ReaderTextAlignment.END
        ) {
            return@remember 0.sp
        }
        (mainState.value.paragraphIndentation * 6).sp
    }
    val perceptionExpanderPadding = remember(
        sidePadding,
        mainState.value.perceptionExpanderPadding
    ) {
        sidePadding + (mainState.value.perceptionExpanderPadding * 8).dp
    }
    val perceptionExpanderThickness = remember(
        mainState.value.perceptionExpanderThickness
    ) {
        (mainState.value.perceptionExpanderThickness * 0.25f).dp
    }

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val cutoutInsets = WindowInsets.displayCutout
    val systemBarsInsets = WindowInsets.systemBarsIgnoringVisibility

    val cutoutInsetsPadding = remember(mainState.value.cutoutPadding) {
        derivedStateOf {
            cutoutInsets.asPaddingValues(density = density).run {
                if (mainState.value.cutoutPadding) PaddingValues(
                    top = calculateTopPadding(),
                    start = calculateStartPadding(layoutDirection),
                    end = calculateEndPadding(layoutDirection),
                    bottom = calculateBottomPadding()
                ) else PaddingValues(0.dp)
            }
        }
    }
    val systemBarsInsetsPadding = remember(mainState.value.fullscreen) {
        derivedStateOf {
            systemBarsInsets.asPaddingValues(density = density).run {
                if (!mainState.value.fullscreen) PaddingValues(
                    top = calculateTopPadding(),
                    start = calculateStartPadding(layoutDirection),
                    end = calculateEndPadding(layoutDirection),
                    bottom = calculateBottomPadding()
                ) else PaddingValues(0.dp)
            }
        }
    }

    val contentPadding = remember(
        cutoutInsetsPadding.value,
        systemBarsInsetsPadding.value
    ) {
        PaddingValues(
            top = systemBarsInsetsPadding.value.calculateTopPadding().run {
                if (equals(0.dp)) return@run cutoutInsetsPadding.value
                    .calculateTopPadding()
                this
            },
            start = systemBarsInsetsPadding.value.calculateStartPadding(layoutDirection).run {
                if (equals(0.dp)) return@run cutoutInsetsPadding.value
                    .calculateStartPadding(layoutDirection)
                this
            },
            end = systemBarsInsetsPadding.value.calculateEndPadding(layoutDirection).run {
                if (equals(0.dp)) return@run cutoutInsetsPadding.value
                    .calculateEndPadding(layoutDirection)
                this
            },
            bottom = systemBarsInsetsPadding.value.calculateBottomPadding().run {
                if (equals(0.dp)) return@run cutoutInsetsPadding.value
                    .calculateBottomPadding()
                this
            }
        )
    }

    val chapters = remember(state.value.book.chapters) {
        val chapters = mutableMapOf<Int, Chapter>()
        state.value.book.chapters.forEach {
            chapters[it.startIndex] = it
        }
        chapters
    }

    // Bottom sheets & Dialogs
    if (state.value.showSettingsBottomSheet) {
        ReaderSettingsBottomSheet()
    }
    if (state.value.showUpdateDialog) {
        ReaderUpdateDialog()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AnimatedVisibility(
                visible = state.value.showMenu,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                ReaderTopBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = state.value.showMenu,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar()
            }
        }
    ) {
        SelectionContainer(
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
                                .noRippleClickable(
                                    onClick = {
                                        onEvent(
                                            ReaderEvent.OnShowHideMenu(
                                                fullscreenMode = mainState.value.fullscreen,
                                                saveCheckpoint = true,
                                                activity = context
                                            )
                                        )
                                    }
                                )
                        } else {
                            Modifier
                        }
                    )
                    .padding(contentPadding)
                    .padding(vertical = verticalPadding),
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
                itemsIndexed(
                    state.value.text, key = { index, _ -> index }
                ) { index, line ->
                    ReaderChapter(
                        chapter = chapters[index], // Shows only when matching startIndex of chapter.
                        fontColor = fontColor.value,
                        sidePadding = sidePadding
                    )

                    ReaderTextParagraph(
                        line = line,
                        fontFamily = fontFamily,
                        fontColor = fontColor.value,
                        lineHeight = lineHeight,
                        fontStyle = fontStyle,
                        textAlignment = mainState.value.textAlignment,
                        fontSize = mainState.value.fontSize.sp,
                        letterSpacing = letterSpacing,
                        sidePadding = sidePadding,
                        paragraphIndentation = paragraphIndentation,
                        fullscreenMode = mainState.value.fullscreen,
                        doubleClickTranslationEnabled = mainState.value.doubleClickTranslation,
                        toolbarHidden = toolbarHidden
                    )
                }
            }
        }

        ReaderPerceptionExpander(
            perceptionExpander = mainState.value.perceptionExpander,
            sidePadding = perceptionExpanderPadding,
            thickness = perceptionExpanderThickness,
            lineColor = fontColor.value
        )
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
                ErrorPlaceholder(
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
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.5.dp
                )
            }
        }
    }

    // Drawers
    ReaderChaptersDrawer()

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