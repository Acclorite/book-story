/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.model.common.NullableBook
import ua.acclorite.book_story.presentation.browse.BrowseEvent
import ua.acclorite.book_story.presentation.library.model.SelectableNullableBook
import ua.acclorite.book_story.ui.common.components.dialog.Dialog
import ua.acclorite.book_story.ui.common.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.ui.common.helpers.showToast
import java.util.UUID

@Composable
fun BrowseAddDialog(
    loadingAddDialog: Boolean,
    selectedBooksAddDialog: List<SelectableNullableBook>,
    dismissAddDialog: (BrowseEvent.OnDismissAddDialog) -> Unit,
    actionAddDialog: (BrowseEvent.OnActionAddDialog) -> Unit,
    selectAddDialog: (BrowseEvent.OnSelectAddDialog) -> Unit
) {
    val context = LocalContext.current
    Dialog(
        title = stringResource(id = R.string.add_books),
        icon = Icons.Default.AddChart,
        description = stringResource(id = R.string.add_books_description),
        actionEnabled = !loadingAddDialog && selectedBooksAddDialog.any { it.data is NullableBook.NotNull },
        onDismiss = { dismissAddDialog(BrowseEvent.OnDismissAddDialog) },
        onAction = {
            actionAddDialog(BrowseEvent.OnActionAddDialog)
        },
        withContent = true,
        items = {
            if (loadingAddDialog) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }
                }
            } else {
                items(
                    selectedBooksAddDialog,
                    key = { it.data.fileName ?: UUID.randomUUID() }
                ) { book ->
                    BrowseAddDialogItem(
                        result = book
                    ) {
                        if (it) {
                            selectAddDialog(
                                BrowseEvent.OnSelectAddDialog(
                                    book = book
                                )
                            )
                        } else {
                            book.data.message?.asString(context)
                                ?.showToast(context = context)
                        }
                    }
                }
            }
        }
    )
}