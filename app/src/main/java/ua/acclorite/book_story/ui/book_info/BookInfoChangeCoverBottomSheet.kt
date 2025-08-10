/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.common.components.modal_bottom_sheet.ModalBottomSheet

@Composable
fun BookInfoChangeCoverBottomSheet(
    book: Book,
    canResetCover: Boolean,
    changeCover: (BookInfoEvent.OnChangeCover) -> Unit,
    resetCover: (BookInfoEvent.OnResetCover) -> Unit,
    deleteCover: (BookInfoEvent.OnDeleteCover) -> Unit,
    checkCoverReset: (BookInfoEvent.OnCheckCoverReset) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit
) {
    val context = LocalContext.current
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val image = context.contentResolver?.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                } ?: return@rememberLauncherForActivityResult

                changeCover(
                    BookInfoEvent.OnChangeCover(
                        image = image
                    )
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        checkCoverReset(BookInfoEvent.OnCheckCoverReset)
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            dismissBottomSheet(BookInfoEvent.OnDismissBottomSheet)
        },
        sheetGesturesEnabled = true
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (canResetCover) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.Restore,
                        text = stringResource(id = R.string.reset_cover),
                        description = stringResource(id = R.string.reset_cover_desc)
                    ) {
                        resetCover(BookInfoEvent.OnResetCover)
                    }
                }
            }

            item {
                BookInfoChangeCoverBottomSheetItem(
                    icon = Icons.Default.ImageSearch,
                    text = stringResource(id = R.string.change_cover),
                    description = stringResource(id = R.string.change_cover_desc)
                ) {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            }

            if (book.coverImage != null) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.HideImage,
                        text = stringResource(id = R.string.delete_cover),
                        description = stringResource(id = R.string.delete_cover_desc)
                    ) {
                        deleteCover(BookInfoEvent.OnDeleteCover)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}