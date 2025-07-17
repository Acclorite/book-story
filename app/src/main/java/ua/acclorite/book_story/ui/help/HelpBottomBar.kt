/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.help

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun HelpBottomBar(
    changeShowStartScreen: (Boolean) -> Unit,
    navigateToBrowse: () -> Unit
) {
    Button(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(top = 18.dp, bottom = 8.dp)
            .padding(horizontal = 18.dp)
            .fillMaxWidth(),
        onClick = {
            BrowseScreen.refreshListChannel.trySend(Unit)
            changeShowStartScreen(false)
            navigateToBrowse()
        }
    ) {
        StyledText(text = stringResource(id = R.string.done))
    }
}