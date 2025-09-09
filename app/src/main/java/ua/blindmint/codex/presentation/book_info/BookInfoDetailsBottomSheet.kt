/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.file.CachedFileCompat
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.presentation.core.components.common.LazyColumnWithScrollbar
import ua.blindmint.codex.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import ua.blindmint.codex.presentation.core.constants.provideExtensions
import ua.blindmint.codex.presentation.settings.components.SettingsSubcategoryTitle
import ua.blindmint.codex.ui.book_info.BookInfoEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookInfoDetailsBottomSheet(
    book: Book,
    showPathDialog: (BookInfoEvent.OnShowPathDialog) -> Unit,
    clearProgressHistory: (BookInfoEvent.OnClearProgressHistory) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit
) {
    val pattern = remember { SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()) }
    val lastOpened = remember(book.lastOpened) { pattern.format(Date(book.lastOpened ?: 0)) }

    val context = LocalContext.current
    val cachedFile = remember(book.filePath) {
        CachedFileCompat.fromFullPath(context, book.filePath)
    }

    val fileSize = remember(cachedFile) {
        if (cachedFile != null && cachedFile.canAccess()) {
            val sizeBytes = cachedFile.size
            val sizeKB = sizeBytes / 1024f
            val sizeMB = sizeKB / 1024f
            when {
                sizeMB >= 1f -> "%.2f MB".format(sizeMB)
                sizeKB > 0f -> "%.2f KB".format(sizeKB)
                else -> ""
            }
        } else {
            ""
        }
    }

    val fileExists = remember(cachedFile) {
        cachedFile.let {
            it != null && it.canAccess() && !it.isDirectory && provideExtensions()
                .any { ext ->
                    it.name.endsWith(ext, ignoreCase = true)
                }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            dismissBottomSheet(BookInfoEvent.OnDismissBottomSheet)
        },
        sheetGesturesEnabled = true
    ) {
        LazyColumnWithScrollbar(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            item {
                SettingsSubcategoryTitle(
                    title = stringResource(id = R.string.file_details),
                    padding = 16.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    label = stringResource(id = R.string.file_path),
                    text = book.filePath,
                    editable = true,
                    onEdit = {
                        showPathDialog(BookInfoEvent.OnShowPathDialog)
                    },
                    showError = !fileExists,
                    errorMessage = stringResource(id = R.string.error_no_file)
                )
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    label = stringResource(id = R.string.file_last_opened),
                    text = if (book.lastOpened != null) lastOpened
                    else stringResource(id = R.string.never),
                    editable = false
                )
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    label = stringResource(id = R.string.file_size),
                    text = fileSize.ifBlank { stringResource(id = R.string.unknown) },
                    editable = false
                )
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    label = stringResource(id = R.string.clear_progress_history),
                    text = "",
                    editable = true,
                    onEdit = {
                        clearProgressHistory(BookInfoEvent.OnClearProgressHistory(context))
                    }
                )
            }
        }
    }
}