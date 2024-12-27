package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.reader.Checkpoint
import ua.acclorite.book_story.domain.reader.FontWithName
import ua.acclorite.book_story.domain.reader.ReaderHorizontalGesture
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.domain.util.Drawer
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.settings.SettingsEvent

@Composable
fun ReaderContent(
    book: Book,
    text: List<AnnotatedString>,
    dialog: Dialog?,
    bottomSheet: BottomSheet?,
    drawer: Drawer?,
    listState: LazyListState,
    currentChapter: Chapter?,
    nestedScrollConnection: NestedScrollConnection,
    fastColorPresetChange: Boolean,
    perceptionExpander: Boolean,
    perceptionExpanderPadding: Dp,
    perceptionExpanderThickness: Dp,
    currentChapterProgress: Float,
    isLoading: Boolean,
    errorMessage: UIText?,
    checkpoint: Checkpoint,
    checkingForUpdate: Boolean,
    updateFound: Boolean,
    showMenu: Boolean,
    lockMenu: Boolean,
    chapters: Map<Int, Chapter>,
    contentPadding: PaddingValues,
    verticalPadding: Dp,
    horizontalGesture: ReaderHorizontalGesture,
    horizontalGestureScroll: Float,
    horizontalGestureSensitivity: Dp,
    paragraphHeight: Dp,
    sidePadding: Dp,
    bottomBarPadding: Dp,
    backgroundColor: Color,
    fontColor: Color,
    fontFamily: FontWithName,
    lineHeight: TextUnit,
    fontStyle: FontStyle,
    textAlignment: ReaderTextAlignment,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    paragraphIndentation: TextUnit,
    doubleClickTranslation: Boolean,
    fullscreenMode: Boolean,
    selectPreviousPreset: (SettingsEvent.OnSelectPreviousPreset) -> Unit,
    selectNextPreset: (SettingsEvent.OnSelectNextPreset) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    leave: (ReaderEvent.OnLeave) -> Unit,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    changeProgress: (ReaderEvent.OnChangeProgress) -> Unit,
    openShareApp: (ReaderEvent.OnOpenShareApp) -> Unit,
    openWebBrowser: (ReaderEvent.OnOpenWebBrowser) -> Unit,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    openDictionary: (ReaderEvent.OnOpenDictionary) -> Unit,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    updateText: (ReaderEvent.OnUpdateText) -> Unit,
    dismissDialog: (ReaderEvent.OnDismissDialog) -> Unit,
    cancelCheckForTextUpdate: (ReaderEvent.OnCancelCheckForTextUpdate) -> Unit,
    showUpdateDialog: (ReaderEvent.OnShowUpdateDialog) -> Unit,
    showSettingsBottomSheet: (ReaderEvent.OnShowSettingsBottomSheet) -> Unit,
    dismissBottomSheet: (ReaderEvent.OnDismissBottomSheet) -> Unit,
    showChaptersDrawer: (ReaderEvent.OnShowChaptersDrawer) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit,
    navigateBack: () -> Unit,
    navigateToBookInfo: (startUpdate: Boolean) -> Unit
) {
    ReaderDialog(
        dialog = dialog,
        updateText = updateText,
        dismissDialog = dismissDialog,
        navigateToBookInfo = navigateToBookInfo
    )

    ReaderBottomSheet(
        bottomSheet = bottomSheet,
        fullscreenMode = fullscreenMode,
        menuVisibility = menuVisibility,
        dismissBottomSheet = dismissBottomSheet
    )

    ReaderScaffold(
        book = book,
        text = text,
        listState = listState,
        currentChapter = currentChapter,
        nestedScrollConnection = nestedScrollConnection,
        fastColorPresetChange = fastColorPresetChange,
        perceptionExpander = perceptionExpander,
        perceptionExpanderPadding = perceptionExpanderPadding,
        perceptionExpanderThickness = perceptionExpanderThickness,
        currentChapterProgress = currentChapterProgress,
        isLoading = isLoading,
        errorMessage = errorMessage,
        checkpoint = checkpoint,
        checkingForUpdate = checkingForUpdate,
        updateFound = updateFound,
        showMenu = showMenu,
        lockMenu = lockMenu,
        chapters = chapters,
        contentPadding = contentPadding,
        verticalPadding = verticalPadding,
        horizontalGesture = horizontalGesture,
        horizontalGestureScroll = horizontalGestureScroll,
        horizontalGestureSensitivity = horizontalGestureSensitivity,
        paragraphHeight = paragraphHeight,
        sidePadding = sidePadding,
        bottomBarPadding = bottomBarPadding,
        backgroundColor = backgroundColor,
        fontColor = fontColor,
        fontFamily = fontFamily,
        lineHeight = lineHeight,
        fontStyle = fontStyle,
        textAlignment = textAlignment,
        fontSize = fontSize,
        letterSpacing = letterSpacing,
        paragraphIndentation = paragraphIndentation,
        doubleClickTranslation = doubleClickTranslation,
        fullscreenMode = fullscreenMode,
        selectPreviousPreset = selectPreviousPreset,
        selectNextPreset = selectNextPreset,
        menuVisibility = menuVisibility,
        leave = leave,
        restoreCheckpoint = restoreCheckpoint,
        scroll = scroll,
        changeProgress = changeProgress,
        openShareApp = openShareApp,
        openWebBrowser = openWebBrowser,
        openTranslator = openTranslator,
        openDictionary = openDictionary,
        cancelCheckForTextUpdate = cancelCheckForTextUpdate,
        showUpdateDialog = showUpdateDialog,
        showSettingsBottomSheet = showSettingsBottomSheet,
        showChaptersDrawer = showChaptersDrawer,
        navigateBack = navigateBack,
        navigateToBookInfo = navigateToBookInfo
    )

    ReaderDrawer(
        drawer = drawer,
        chapters = chapters.values.toList(),
        currentChapter = currentChapter,
        currentChapterProgress = currentChapterProgress,
        scrollToChapter = scrollToChapter,
        dismissDrawer = dismissDrawer
    )

    ReaderBackHandler(
        leave = leave,
        navigateBack = navigateBack
    )
}