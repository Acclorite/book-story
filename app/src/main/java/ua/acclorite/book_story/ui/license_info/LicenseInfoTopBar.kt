/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.license_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.about.AboutEvent
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.navigator.NavigatorBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseInfoTopBar(
    library: Library,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateBack: (AboutEvent.OnNavigateBack) -> Unit
) {
    LargeTopAppBar(
        title = {
            StyledText(
                text = library.name
            )
        },
        navigationIcon = {
            NavigatorBackIconButton(navigateBack = { navigateBack(AboutEvent.OnNavigateBack) })
        },
        actions = {
            if (library.website?.isNotBlank() == true) {
                IconButton(
                    icon = Icons.Outlined.Language,
                    contentDescription = R.string.open_in_web_content_desc,
                    disableOnClick = false
                ) {
                    navigateToBrowserPage(
                        AboutEvent.OnNavigateToBrowserPage(
                            page = library.website!!
                        )
                    )
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