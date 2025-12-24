/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun StartDoneBottomBar(
    navigateToBrowse: () -> Unit
) {
    Button(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = 8.dp, top = 18.dp)
            .padding(horizontal = 18.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(100),
        onClick = {
            navigateToBrowse()
        }
    ) {
        StyledText(text = stringResource(id = R.string.done))
    }
}