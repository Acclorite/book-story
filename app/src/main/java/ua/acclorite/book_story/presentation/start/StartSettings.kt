/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.start

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.navigator.StackEvent
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.start.StartEvent
import ua.acclorite.book_story.ui.start.StartScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartSettings(
    currentPage: Int,
    stackEvent: StackEvent,
    storagePermissionGranted: Boolean,
    storagePermissionState: PermissionState,
    languages: List<ButtonItem>,
    changeLanguage: (MainEvent.OnChangeLanguage) -> Unit,
    storagePermissionRequest: (StartEvent.OnStoragePermissionRequest) -> Unit,
    navigateForward: () -> Unit
) {
    val activity = LocalActivity.current

    StartSettingsScaffold(
        currentPage = currentPage,
        storagePermissionGranted = storagePermissionGranted,
        navigateForward = navigateForward
    ) {
        StartContentTransition(
            targetValue = when (currentPage) {
                0 -> StartScreen.GENERAL_SETTINGS
                1 -> StartScreen.APPEARANCE_SETTINGS
                else -> StartScreen.PERMISSION_SETTINGS
            },
            stackEvent = stackEvent
        ) { page ->
            StartSettingsLayout {
                when (page) {
                    StartScreen.GENERAL_SETTINGS -> {
                        StartSettingsLayoutGeneral(
                            languages = languages,
                            changeLanguage = changeLanguage
                        )
                    }

                    StartScreen.APPEARANCE_SETTINGS -> {
                        StartSettingsLayoutAppearance()
                    }

                    StartScreen.PERMISSION_SETTINGS -> {
                        StartSettingsLayoutPermissions(
                            activity = activity,
                            storagePermissionGranted = storagePermissionGranted,
                            storagePermissionState = storagePermissionState,
                            storagePermissionRequest = storagePermissionRequest
                        )
                    }
                }
            }
        }
    }
}