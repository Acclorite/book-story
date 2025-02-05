/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideAboutBadges
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.about.AboutEvent

@Composable
fun AboutBadges(
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit
) {
    val context = LocalContext.current

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(
            Constants.provideAboutBadges(),
            key = { it.id }
        ) { badge ->
            AboutBadgeItem(badge = badge) {
                when (badge.id) {
                    "tryzub" -> {
                        context.getString(R.string.slava_ukraini)
                            .showToast(context = context, longToast = false)
                    }

                    else -> {
                        badge.url?.let {
                            navigateToBrowserPage(
                                AboutEvent.OnNavigateToBrowserPage(
                                    page = it,
                                    context = context
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}