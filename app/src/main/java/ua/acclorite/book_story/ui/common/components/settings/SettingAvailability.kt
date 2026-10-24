/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderLayout
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.helpers.showToast

@Composable
fun SettingAvailability(
    unavailableReason: String?,
    content: @Composable () -> Unit
) {
    if (unavailableReason == null) {
        content()
        return
    }

    val context = LocalContext.current
    Box(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .alpha(0.38f)
                .focusProperties { canFocus = false }
                .clearAndSetSemantics { }
        ) {
            content()
        }
        Box(
            Modifier
                .matchParentSize()
                .clickable(onClickLabel = unavailableReason) {
                    unavailableReason.showToast(context, longToast = false)
                }
                .clearAndSetSemantics {
                    contentDescription = unavailableReason
                    onClick {
                        unavailableReason.showToast(context, longToast = false)
                        true
                    }
                }
        )
    }
}

@Composable
fun ContinuousLayoutOnlySetting(content: @Composable () -> Unit) {
    val layout = LocalSettings.current.readerLayout.value
    SettingAvailability(
        unavailableReason = if (layout == ReaderLayout.CONTINUOUS) null
        else stringResource(R.string.continuous_layout_only_setting),
        content = content
    )
}
