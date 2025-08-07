/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.credits

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.presentation.about.AboutEvent
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.credits.data.CreditData

@Composable
fun CreditsLayout(
    paddingValues: PaddingValues,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit
) {
    LazyColumnWithScrollbar(
        Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        state = listState
    ) {
        items(
            CreditData.credits,
            key = { it.name }
        ) { credit ->
            CreditItem(credit = credit) {
                credit.website?.let { website ->
                    navigateToBrowserPage(
                        AboutEvent.OnNavigateToBrowserPage(
                            page = website
                        )
                    )
                }
            }
        }
    }
}