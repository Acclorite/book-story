/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.blindmint.codex.presentation.settings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun LazyListScope.SettingsSubcategory(
    titleColor: @Composable () -> Color,
    title: @Composable () -> String,
    showTitle: Boolean,
    showDivider: Boolean,
    content: LazyListScope.() -> Unit
) {
    item {
        if (showTitle) {
            SettingsSubcategoryTitle(
                title = title.invoke(),
                color = titleColor.invoke(),
                modifier = Modifier
                    .padding(
                        top = 18.dp,
                        bottom = 8.dp
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }
    }

    content()

    item {
        Spacer(modifier = Modifier.height(18.dp))
        if (showDivider) HorizontalDivider()
    }
}