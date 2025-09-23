/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.getOrElse
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.book_info.BookInfoContent
import ua.acclorite.book_story.ui.book_info.BookInfoEffects

@Parcelize
data class BookInfoScreen(val bookId: Int) : Screen, Parcelable {

    companion object {
        const val DELETE_DIALOG = "delete_dialog"
        const val MOVE_DIALOG = "move_dialog"
        const val TITLE_DIALOG = "title_dialog"
        const val AUTHOR_DIALOG = "author_dialog"
        const val DESCRIPTION_DIALOG = "description_dialog"
        const val PATH_DIALOG = "path_dialog"

        const val CHANGE_COVER_BOTTOM_SHEET = "change_cover_bottom_sheet"
        const val DETAILS_BOTTOM_SHEET = "details_bottom_sheet"

        val changePathChannel: Channel<Boolean> = Channel(Channel.CONFLATED)
    }

    @Composable
    override fun Content() {
        val screenModel = hiltViewModel<BookInfoModel>()
        val settingsModel = hiltViewModel<SettingsModel>()

        val settingsState = settingsModel.state.collectAsStateWithLifecycle()
        val state = screenModel.state.collectAsStateWithLifecycle()
        val listState = rememberLazyListState()

        val categories = remember(settingsState.value.categories) {
            settingsState.value.categories.filterNot { it.id == -1 }
        }

        LaunchedEffect(Unit) {
            screenModel.init(
                bookId = bookId,
                changePath = changePathChannel.tryReceive().getOrElse { false }
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                screenModel.clearAsync()
            }
        }

        BookInfoEffects(
            effects = screenModel.effects,
            book = state.value.book
        )

        Box(Modifier.fillMaxSize())

        if (state.value.book.id == bookId) {
            BookInfoContent(
                book = state.value.book,
                file = state.value.file,
                categories = categories,
                bottomSheet = state.value.bottomSheet,
                dialog = state.value.dialog,
                listState = listState,
                canResetCover = state.value.canResetCover,
                showTitleDialog = screenModel::onEvent,
                actionTitleDialog = screenModel::onEvent,
                showAuthorDialog = screenModel::onEvent,
                actionAuthorDialog = screenModel::onEvent,
                showDescriptionDialog = screenModel::onEvent,
                actionDescriptionDialog = screenModel::onEvent,
                showPathDialog = screenModel::onEvent,
                actionPathDialog = screenModel::onEvent,
                checkCoverReset = screenModel::onEvent,
                changeCover = screenModel::onEvent,
                resetCover = screenModel::onEvent,
                deleteCover = screenModel::onEvent,
                dismissBottomSheet = screenModel::onEvent,
                dismissDialog = screenModel::onEvent,
                showChangeCoverBottomSheet = screenModel::onEvent,
                showDetailsBottomSheet = screenModel::onEvent,
                showMoveDialog = screenModel::onEvent,
                actionMoveDialog = screenModel::onEvent,
                showDeleteDialog = screenModel::onEvent,
                actionDeleteDialog = screenModel::onEvent,
                navigateToReader = screenModel::onEvent,
                navigateToLibrarySettings = screenModel::onEvent,
                navigateBack = screenModel::onEvent
            )
        }
    }
}