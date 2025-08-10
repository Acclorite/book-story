/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.history

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.history.HistoryEffect
import ua.acclorite.book_story.presentation.history.HistoryEvent
import ua.acclorite.book_story.presentation.history.HistoryScreen.insertHistoryChannel
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.reader.ReaderScreen
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Composable
fun HistoryEffects(
    effects: SharedFlow<HistoryEffect>,
    focusRequester: FocusRequester,
    snackbarState: SnackbarHostState,
    restoreHistoryEntry: (HistoryEvent.OnRestoreHistoryEntry) -> Unit
) {
    val navigator = LocalNavigator.current
    val context = LocalContext.current

    LaunchedEffect(effects, context, navigator, focusRequester, snackbarState) {
        effects.collect { effect ->
            when (effect) {
                is HistoryEffect.OnRequestFocus -> {
                    focusRequester.requestFocus()
                }

                is HistoryEffect.OnShowSnackbar -> {
                    val snackbarResult = snackbarState.showSnackbar(
                        context.getString(R.string.history_element_deleted),
                        context.getString(R.string.undo)
                    )

                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        restoreHistoryEntry(
                            HistoryEvent.OnRestoreHistoryEntry(
                                history = effect.history
                            )
                        )
                    }
                }

                is HistoryEffect.OnWholeHistoryDeleted -> {
                    context.getString(R.string.history_deleted)
                        .showToast(context = context)
                }

                is HistoryEffect.OnNavigateToLibrary -> {
                    navigator.push(LibraryScreen, saveInBackStack = false)
                }

                is HistoryEffect.OnNavigateToBookInfo -> {
                    navigator.push(BookInfoScreen(bookId = effect.bookId))
                }

                is HistoryEffect.OnNavigateToReader -> {
                    insertHistoryChannel.trySend(effect.bookId)
                    navigator.push(ReaderScreen(bookId = effect.bookId))
                }
            }
        }
    }
}