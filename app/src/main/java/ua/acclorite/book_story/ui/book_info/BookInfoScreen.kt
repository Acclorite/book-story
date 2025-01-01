package ua.acclorite.book_story.ui.book_info

import android.os.Parcelable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        const val DELETE_DIALOG = "delete_dialog"
        const val MOVE_DIALOG = "move_dialog"

        const val CHANGE_COVER_BOTTOM_SHEET = "change_cover_bottom_sheet"
        const val MORE_BOTTOM_SHEET = "more_bottom_sheet"
        const val DETAILS_BOTTOM_SHEET = "details_bottom_sheet"
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = hiltViewModel<BookInfoModel>()

        val state = screenModel.state.collectAsStateWithLifecycle()
        val listState = rememberLazyListState()

        LaunchedEffect(Unit) {
            screenModel.init(
                bookId = bookId,
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
            listState = listState,
            canResetCover = state.value.canResetCover,
            editTitle = state.value.editTitle,
            titleValue = state.value.titleValue,
            editAuthor = state.value.editAuthor,
            authorValue = state.value.authorValue,
            editDescription = state.value.editDescription,
            descriptionValue = state.value.descriptionValue,
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
            updateData = screenModel::onEvent,
            copyToClipboard = screenModel::onEvent,
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