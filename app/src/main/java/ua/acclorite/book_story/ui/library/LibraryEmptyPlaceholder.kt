/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.ui.common.components.placeholder.EmptyPlaceholder
import ua.acclorite.book_story.ui.theme.Transitions

@Composable
fun BoxScope.LibraryEmptyPlaceholder(
    isLoading: Boolean,
    isRefreshing: Boolean,
    isBooksEmpty: Boolean,
    navigateToBrowse: (LibraryEvent.OnNavigateToBrowse) -> Unit
) {
    AnimatedVisibility(
        visible = !isLoading
                && !isRefreshing
                && isBooksEmpty,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = fadeOut(tween(0))
    ) {
        EmptyPlaceholder(
            message = stringResource(id = R.string.library_empty),
            icon = painterResource(id = R.drawable.empty_library),
            modifier = Modifier.align(Alignment.Center),
            actionTitle = stringResource(id = R.string.add_book)
        ) {
            navigateToBrowse(LibraryEvent.OnNavigateToBrowse)
        }
    }
}