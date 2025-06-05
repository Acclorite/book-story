/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.navigator

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.about.AboutScreen
import ua.acclorite.book_story.presentation.help.HelpScreen
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
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                NavigatorBottomSheetItem(
                    title = stringResource(id = R.string.about_screen),
                    primary = false,
                    position = Position.TOP
                ) {
                    navigator.push(AboutScreen)
                    navigatorBottomSheetChannel.trySend(false)
                }
            }

            item {
                NavigatorBottomSheetItem(
                    title = stringResource(id = R.string.help_screen),
                    primary = false,
                    position = Position.BOTTOM
                ) {
                    navigator.push(HelpScreen(fromStart = false))
                    navigatorBottomSheetChannel.trySend(false)
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
            }

            item {
                NavigatorBottomSheetItem(
                    title = stringResource(id = R.string.settings_screen),
                    primary = true,
                    position = Position.SOLO
                ) {
                    navigator.push(SettingsScreen)
                    navigatorBottomSheetChannel.trySend(false)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}