/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.ui.browse.BrowseScreen
import ua.blindmint.codex.ui.main.MainEvent

@Composable
fun HelpBottomBar(
    changeShowStartScreen: (MainEvent.OnChangeShowStartScreen) -> Unit,
    navigateToBrowse: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .padding(horizontal = 18.dp)
                .fillMaxWidth(),
            onClick = {
                BrowseScreen.refreshListChannel.trySend(Unit)
                changeShowStartScreen(MainEvent.OnChangeShowStartScreen(false))
                navigateToBrowse()
            }
        ) {
            StyledText(text = stringResource(id = R.string.done))
        }
    }
}