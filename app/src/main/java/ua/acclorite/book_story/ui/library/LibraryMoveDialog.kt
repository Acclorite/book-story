/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.components.dialog.CheckboxDialogItem
import ua.acclorite.book_story.ui.common.components.dialog.Dialog

@Composable
fun LibraryMoveDialog(
    books: List<SelectableBook>,
    categories: List<Category>,
    actionMoveDialog: (LibraryEvent.OnActionMoveDialog) -> Unit,
    dismissDialog: (LibraryEvent.OnDismissDialog) -> Unit,
    navigateToLibrarySettings: (LibraryEvent.OnNavigateToLibrarySettings) -> Unit
) {
    val selectedBooks = remember {
        derivedStateOf {
            books.filter { it.selected }
        }
    }
    val selectedCategories = remember {
        mutableStateListOf<Category>().apply {
            clear()
            categories.filterNot { it.id == -1 }.forEach { category ->
                if (selectedBooks.value.all { it.data.categories.any { it == category.id } }) {
                    add(category)
                }
            }
        }
    }

    Dialog(
        title = stringResource(id = R.string.move),
        description = null,
        actionEnabled = true,
        onDismiss = {
            dismissDialog(LibraryEvent.OnDismissDialog)
        },
        onAction = {
            actionMoveDialog(
                LibraryEvent.OnActionMoveDialog(
                    selectedCategories = selectedCategories
                )
            )
        },
        secondaryAction = stringResource(id = R.string.edit),
        onSecondaryAction = {
            navigateToLibrarySettings(LibraryEvent.OnNavigateToLibrarySettings)
            dismissDialog(LibraryEvent.OnDismissDialog)
        },
        withContent = true,
        items = {
            items(categories.filterNot { it.id == -1 }) { category ->
                val selected = remember(
                    category,
                    selectedCategories
                ) {
                    derivedStateOf {
                        selectedCategories.any { it == category }
                    }
                }

                CheckboxDialogItem(
                    selected = selected.value,
                    title = category.title
                ) {
                    if (selected.value) selectedCategories.remove(category)
                    else selectedCategories.add(category)
                }
            }

            if (categories.isEmpty()) {
                item {
                    StyledText(
                        text = stringResource(id = R.string.categories_empty),
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    )
}