/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.IconButton

@Composable
fun NavigatorBackIconButton(
    enabled: Boolean = true,
    navigateBack: () -> Unit,
) {
    IconButton(
        icon = Icons.AutoMirrored.Outlined.ArrowBack,
        contentDescription = R.string.go_back_content_desc,
        disableOnClick = true,
        enabled = enabled
    ) {
        navigateBack()
    }
}