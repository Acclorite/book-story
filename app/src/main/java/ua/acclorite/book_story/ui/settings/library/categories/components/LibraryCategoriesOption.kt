/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.categories.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.automirrored.outlined.LabelOff
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sh.calvin.reorderable.ReorderableColumn
import sh.calvin.reorderable.ReorderableScope
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.components.dialog.Dialog
import ua.acclorite.book_story.ui.common.components.dialog.DialogWithTextField
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.theme.ExpandingTransition
import ua.acclorite.book_story.ui.theme.dynamicListItemColor

private const val CREATE_DIALOG = "create_dialog"
private const val EDIT_DIALOG = "edit_dialog"
private const val REMOVE_DIALOG = "remove_dialog"

@Composable
fun LibraryCategoriesOption() {
    val settingsModel = hiltViewModel<SettingsModel>()
    val state = settingsModel.state.collectAsStateWithLifecycle()

    val categories = remember(state.value.categories) {
        state.value.categories.filterNot { it.id == -1 }
    }
    val dialog = remember {
        mutableStateOf<String?>(null)
    }
    val selectedCategory = remember {
        mutableStateOf<Category?>(null)
    }

    when (dialog.value) {
        CREATE_DIALOG -> {
            LibraryCategoriesCreateDialog(
                onDismiss = { dialog.value = null },
                onAction = { title ->
                    settingsModel.onEvent(
                        SettingsEvent.OnCreateCategory(
                            title = title
                        )
                    )
                }
            )
        }

        EDIT_DIALOG -> {
            LibraryCategoriesEditDialog(
                initialValue = selectedCategory.value?.title ?: "",
                onDismiss = { dialog.value = null },
                onAction = { title ->
                    selectedCategory.value?.let { selectedCategory ->
                        settingsModel.onEvent(
                            SettingsEvent.OnUpdateCategory(
                                category = selectedCategory.copy(
                                    title = title
                                )
                            )
                        )
                    }
                }
            )
        }

        REMOVE_DIALOG -> {
            LibraryCategoriesRemoveDialog(
                onDismiss = { dialog.value = null },
                onAction = {
                    selectedCategory.value?.let { selectedCategory ->
                        LibraryScreen.scrollToPageCompositionChannel.trySend(0)
                        settingsModel.onEvent(
                            SettingsEvent.OnRemoveCategory(selectedCategory)
                        )
                    }
                    dialog.value = null
                }
            )
        }
    }

    ReorderableColumn(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        list = categories,
        onSettle = { from, to ->
            LibraryScreen.scrollToPageCompositionChannel.trySend(0)
            settingsModel.onEvent(
                SettingsEvent.OnUpdateCategoryOrder(
                    categories.toMutableList().apply {
                        add(to, removeAt(from))
                    }
                )
            )
        }
    ) { _, category, _ ->
        key(category.id) {
            LibraryCategoryItem(
                category = category,
                edit = {
                    selectedCategory.value = category
                    dialog.value = EDIT_DIALOG
                },
                remove = {
                    selectedCategory.value = category
                    dialog.value = REMOVE_DIALOG
                }
            )
        }
    }

    LibraryCategoriesAction {
        dialog.value = CREATE_DIALOG
    }
}

@Composable
private fun LibraryCategoriesEditDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onAction: (title: String) -> Unit
) {
    DialogWithTextField(
        initialValue = initialValue,
        lengthLimit = 200,
        onDismiss = {
            onDismiss()
        },
        onAction = {
            if (it.isBlank()) return@DialogWithTextField
            onAction(it.replace("\n", "").trim())
        }
    )
}

@Composable
private fun LibraryCategoriesRemoveDialog(
    onDismiss: () -> Unit,
    onAction: () -> Unit
) {
    Dialog(
        title = stringResource(id = R.string.remove_category),
        icon = Icons.AutoMirrored.Outlined.LabelOff,
        description = stringResource(id = R.string.remove_category_description),
        actionEnabled = true,
        onDismiss = {
            onDismiss()
        },
        onAction = {
            onAction()
        },
        withContent = false
    )
}

@Composable
private fun LibraryCategoriesCreateDialog(
    onDismiss: () -> Unit,
    onAction: (title: String) -> Unit
) {
    val title = remember {
        mutableStateOf("")
    }
    val error = remember {
        mutableStateOf(false)
    }

    val focusRequester = remember { FocusRequester() }
    val focused = remember { mutableStateOf(false) }

    Dialog(
        title = stringResource(id = R.string.create_category),
        description = null,
        onDismiss = { onDismiss() },
        actionEnabled = true,
        disableOnClick = false,
        onAction = {
            if (title.value.isBlank()) {
                error.value = true
                return@Dialog
            }

            onAction(title.value.replace("\n", "").trim())
            onDismiss()
        },
        withContent = true,
        items = {
            item {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = {
                        error.value = false
                        if (it.length <= 200) {
                            title.value = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                    isError = error.value,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            if (!focused.value) {
                                focusRequester.requestFocus()
                                focused.value = true
                            }
                        },
                    colors = TextFieldDefaults.colors(
                        errorContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    label = {
                        StyledText(
                            text = stringResource(
                                id = R.string.library_categories_create_category_placeholder
                            ),
                            maxLines = 1
                        )
                    },
                    supportingText = {
                        ExpandingTransition(error.value) {
                            StyledText(
                                text = stringResource(id = R.string.error_field_empty),
                                style = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun LibraryCategoriesAction(
    create: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(18.dp)
            .noRippleClickable {
                create()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        StyledText(
            text = stringResource(id = R.string.create_category),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
private fun ReorderableScope.LibraryCategoryItem(
    category: Category,
    edit: () -> Unit,
    remove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Label,
            contentDescription = stringResource(id = R.string.drag_content_desc),
            modifier = Modifier
                .clip(CircleShape)
                .draggableHandle()
                .background(MaterialTheme.colorScheme.dynamicListItemColor(category.order))
                .padding(11.dp)
                .size(22.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(18.dp))

        StyledText(
            text = category.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(18.dp))

        IconButton(
            modifier = Modifier.size(24.dp),
            icon = Icons.Outlined.EditNote,
            contentDescription = R.string.edit_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurface
        ) {
            edit()
        }

        IconButton(
            modifier = Modifier.size(24.dp),
            icon = Icons.Outlined.Clear,
            contentDescription = R.string.remove_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurface
        ) {
            remove()
        }
    }
}