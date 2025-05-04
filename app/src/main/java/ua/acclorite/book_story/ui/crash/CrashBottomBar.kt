/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.crash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun CrashBottomBar(
    copy: () -> Unit,
    report: () -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val copyWidth = remember(windowInfo.containerSize) {
        with(density) {
            (windowInfo.containerSize.width.toDp() - 36.dp) * (1f / 2.5f)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(top = 18.dp, bottom = 8.dp)
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            modifier = Modifier.widthIn(
                min = copyWidth
            ),
            onClick = { copy() }
        ) {
            StyledText(
                text = stringResource(id = R.string.copy),
                maxLines = 1
            )
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { report() }
        ) {
            StyledText(
                text = stringResource(id = R.string.report),
                maxLines = 1
            )
        }
    }
}