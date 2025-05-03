/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.presentation.common.components.placeholder.ErrorPlaceholder
import ua.acclorite.book_story.presentation.common.components.top_bar.TopAppBar
import ua.acclorite.book_story.presentation.common.components.top_bar.TopAppBarData
import ua.acclorite.book_story.presentation.common.util.LocalActivity
import ua.acclorite.book_story.presentation.navigator.NavigatorBackIconButton
import ua.acclorite.book_story.ui.reader.ReaderEvent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderErrorPlaceholder(
    errorMessage: UIText,
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateToBookInfo: (changePath: Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    val activity = LocalActivity.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                containerColor = Color.Transparent,
                scrollBehavior = null,
                isTopBarScrolled = null,

                shownTopBar = 0,
                topBars = listOf(
                    TopAppBarData(
                        contentID = 0,
                        contentNavigationIcon = {
                            NavigatorBackIconButton {
                                navigateBack()
                            }
                        },
                        contentTitle = {},
                        contentActions = {}
                    )
                )
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ErrorPlaceholder(
                errorMessage = errorMessage.asString(),
                icon = painterResource(id = R.drawable.error),
                actionTitle = stringResource(id = R.string.change_path),
                action = {
                    leave(
                        ReaderEvent.OnLeave(
                            activity = activity,
                            navigate = {
                                navigateToBookInfo(true)
                            }
                        )
                    )
                }
            )
        }
    }
}