/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.navigator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.about.AboutScreen
import ua.acclorite.book_story.presentation.settings.SettingsScreen
import ua.acclorite.book_story.ui.common.components.modal_bottom_sheet.ModalBottomSheet
import ua.acclorite.book_story.ui.common.model.Position

val navigatorBottomSheetChannel = Channel<Boolean>(Channel.CONFLATED)

@Composable
fun NavigatorBottomSheet() {
    val navigator = LocalNavigator.current

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { navigatorBottomSheetChannel.trySend(false) },
        sheetGesturesEnabled = true
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                NavigatorBottomSheetItem(
                    title = stringResource(id = R.string.about_screen),
                    imageVector = Icons.Outlined.Info,
                    primary = false,
                    position = Position.SOLO
                ) {
                    navigator.push(AboutScreen)
                    navigatorBottomSheetChannel.trySend(false)
                }
            }

            item {
                NavigatorBottomSheetItem(
                    title = stringResource(id = R.string.settings_screen),
                    imageVector = Icons.Default.Settings,
                    primary = true,
                    position = Position.SOLO
                ) {
                    navigator.push(SettingsScreen)
                    navigatorBottomSheetChannel.trySend(false)
                }
            }
        }
    }
}