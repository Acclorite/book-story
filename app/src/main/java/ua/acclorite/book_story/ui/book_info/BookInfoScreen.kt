package ua.acclorite.book_story.ui.book_info

import android.os.Parcelable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.getOrElse
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.navigator.Screen
import ua.acclorite.book_story.presentation.book_info.BookInfoContent
import ua.acclorite.book_story.presentation.navigator.LocalNavigator
import ua.acclorite.book_story.ui.history.HistoryScreen
import ua.acclorite.book_story.ui.library.LibraryScreen
import ua.acclorite.book_story.ui.reader.ReaderScreen

@Parcelize
data class BookInfoScreen(val bookId: Int) : Screen, Parcelable {

    companion object {
        const val UPDATE_DIALOG = "update_dialog"
        const val DELETE_DIALOG = "delete_dialog"
        const val MOVE_DIALOG = "move_dialog"

        const val CHANGE_COVER_BOTTOM_SHEET = "change_cover_bottom_sheet"
        const val MORE_BOTTOM_SHEET = "more_bottom_sheet"
        const val DETAILS_BOTTOM_SHEET = "details_bottom_sheet"

        val startUpdateChannel: Channel<Boolean> = Channel(Channel.CONFLATED)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = hiltViewModel<BookInfoModel>()

        val state = screenModel.state.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val snackbarState = remember { SnackbarHostState() }
        val listState = rememberLazyListState()
        val refreshState = rememberPullRefreshState(
            refreshing = state.value.isUpdating || state.value.checkingForUpdate,
            onRefresh = {
                screenModel.onEvent(
                    BookInfoEvent.OnCheckForTextUpdate(
                        snackbarState = snackbarState,
                        context = context
                    )
                )
            }
        )

        LaunchedEffect(Unit) {
            screenModel.init(
                bookId = bookId,
                startUpdate = startUpdateChannel.tryReceive().getOrElse { false },
                snackbarState = snackbarState,
                context = context,
                navigateBack = {
                    navigator.pop()
                }
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                screenModel.resetScreen()
            }
        }

        BookInfoContent(
            book = state.value.book,
            bottomSheet = state.value.bottomSheet,
            dialog = state.value.dialog,
            refreshState = refreshState,
            listState = listState,
            snackbarState = snackbarState,
            canResetCover = state.value.canResetCover,
            editTitle = state.value.editTitle,
            titleValue = state.value.titleValue,
            editAuthor = state.value.editAuthor,
            authorValue = state.value.authorValue,
            editDescription = state.value.editDescription,
            descriptionValue = state.value.descriptionValue,
            isUpdating = state.value.isUpdating,
            checkingForUpdate = state.value.checkingForUpdate,
            editTitleMode = screenModel::onEvent,
            editTitleValueChange = screenModel::onEvent,
            editTitleRequestFocus = screenModel::onEvent,
            editAuthorMode = screenModel::onEvent,
            editAuthorValueChange = screenModel::onEvent,
            editAuthorRequestFocus = screenModel::onEvent,
            editDescriptionMode = screenModel::onEvent,
            editDescriptionValueChange = screenModel::onEvent,
            editDescriptionRequestFocus = screenModel::onEvent,
            checkCoverReset = screenModel::onEvent,
            changeCover = screenModel::onEvent,
            resetCover = screenModel::onEvent,
            deleteCover = screenModel::onEvent,
            dismissBottomSheet = screenModel::onEvent,
            dismissDialog = screenModel::onEvent,
            showChangeCoverBottomSheet = screenModel::onEvent,
            showDetailsBottomSheet = screenModel::onEvent,
            showMoreBottomSheet = screenModel::onEvent,
            showMoveDialog = screenModel::onEvent,
            actionMoveDialog = screenModel::onEvent,
            showDeleteDialog = screenModel::onEvent,
            actionDeleteDialog = screenModel::onEvent,
            actionUpdateDialog = screenModel::onEvent,
            dismissUpdateDialog = screenModel::onEvent,
            updateData = screenModel::onEvent,
            copyToClipboard = screenModel::onEvent,
            checkForTextUpdate = screenModel::onEvent,
            cancelTextUpdate = screenModel::onEvent,
            navigateToReader = {
                if (state.value.book.id != -1) {
                    HistoryScreen.insertHistoryChannel.trySend(state.value.book.id)
                    navigator.push(ReaderScreen(state.value.book.id))
                }
            },
            navigateToLibrary = {
                navigator.push(
                    LibraryScreen,
                    popping = true,
                    saveInBackStack = false
                )
            },
            navigateBack = {
                navigator.pop()
            }
        )
    }
}