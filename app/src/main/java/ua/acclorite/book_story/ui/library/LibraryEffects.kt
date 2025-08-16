/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.library.LibraryEffect
import ua.acclorite.book_story.presentation.reader.ReaderScreen
import ua.acclorite.book_story.presentation.settings.LibrarySettingsScreen
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Composable
fun LibraryEffects(effects: SharedFlow<LibraryEffect>, focusRequester: FocusRequester) {
    val context = LocalContext.current
    val navigator = LocalNavigator.current

    LaunchedEffect(effects, focusRequester, context, navigator) {
        effects.collect { effect ->
            when (effect) {
                is LibraryEffect.OnRequestFocus -> {
                    focusRequester.requestFocus()
                }

                is LibraryEffect.OnBooksMoved -> {
                    context.getString(R.string.books_moved)
                        .showToast(context = context)
                }

                is LibraryEffect.OnBooksDeleted -> {
                    context.getString(R.string.books_deleted)
                        .showToast(context = context)
                }

                is LibraryEffect.OnNavigateToLibrarySettings -> {
                    navigator.push(LibrarySettingsScreen)
                }

                is LibraryEffect.OnNavigateToBrowse -> {
                    navigator.push(BrowseScreen)
                }

                is LibraryEffect.OnNavigateToBookInfo -> {
                    navigator.push(BookInfoScreen(bookId = effect.id))
                }

                is LibraryEffect.OnNavigateToReader -> {
                    HistoryScreen.insertHistoryChannel.trySend(effect.id)
                    navigator.push(ReaderScreen(effect.id))
                }
            }
        }
    }
}