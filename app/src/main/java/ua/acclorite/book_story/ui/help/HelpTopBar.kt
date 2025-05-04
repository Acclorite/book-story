/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.help

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.navigator.NavigatorBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpTopBar(
    fromStart: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToStart: () -> Unit,
    navigateBack: () -> Unit
) {
    LargeTopAppBar(
        title = {
            StyledText(stringResource(id = R.string.help_screen))
        },
        navigationIcon = {
            if (!fromStart) NavigatorBackIconButton(navigateBack = navigateBack)
        },
        actions = {
            if (!fromStart) {
                IconButton(
                    icon = Icons.Outlined.RestartAlt,
                    contentDescription = R.string.reset_start_content_desc,
                    disableOnClick = false
                ) {
                    navigateToStart()
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}