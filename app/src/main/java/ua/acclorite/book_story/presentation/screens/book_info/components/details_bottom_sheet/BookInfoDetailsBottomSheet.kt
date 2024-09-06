package ua.acclorite.book_story.presentation.screens.book_info.components.details_bottom_sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.core.components.custom_bottom_sheet.CustomBottomSheet
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Details bottom sheet. Displays name, path, last opened and size of the file.
 */
@Composable
fun BookInfoDetailsBottomSheet() {
    val state = LocalBookInfoViewModel.current.state
    val onEvent = LocalBookInfoViewModel.current.onEvent
    val context = LocalContext.current

    val pattern = remember {
        SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
    }
    val lastOpened = remember {
        pattern.format(Date(state.value.book.lastOpened ?: 0))
    }

    val sizeBytes = remember {
        val file = File(state.value.book.filePath)
        if (file.exists()) {
            file.length()
        } else 0
    }

    val fileSizeKB = remember {
        if (sizeBytes > 0) sizeBytes.toDouble() / 1024.0 else 0.0
    }
    val fileSizeMB = remember {
        if (sizeBytes > 0) fileSizeKB / 1024.0 else 0.0
    }

    val fileSize = remember {
        if (fileSizeMB >= 1.0) "%.2f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.2f KB".format(fileSizeKB)
        else ""
    }

    CustomBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onEvent(BookInfoEvent.OnShowHideDetailsBottomSheet)
        }
    ) {
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_name),
            description = state.value.book.filePath.substringAfterLast("/").trim()
        ) {
            onEvent(
                BookInfoEvent.OnCopyToClipboard(
                    context,
                    state.value.book.filePath.substringAfterLast("/").trim(),
                    success = {
                        context.getString(R.string.copied)
                            .showToast(context = context, longToast = false)
                    }
                ))
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_path),
            description = state.value.book.filePath.trim()
        ) {
            onEvent(
                BookInfoEvent.OnCopyToClipboard(
                    context,
                    state.value.book.filePath.trim(),
                    success = {
                        context.getString(R.string.copied)
                            .showToast(context = context, longToast = false)
                    }
                ))
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_last_opened),
            description = if (state.value.book.lastOpened != null) lastOpened
            else stringResource(id = R.string.never)
        ) {
            if (state.value.book.lastOpened != null) {
                onEvent(
                    BookInfoEvent.OnCopyToClipboard(
                        context,
                        lastOpened,
                        success = {
                            context.getString(R.string.copied)
                                .showToast(context = context, longToast = false)
                        }
                    ))
            }
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_size),
            description = fileSize.ifBlank { stringResource(id = R.string.unknown) }
        ) {
            if (fileSize.isNotBlank()) {
                onEvent(
                    BookInfoEvent.OnCopyToClipboard(
                        context,
                        fileSize,
                        success = {
                            context.getString(R.string.copied)
                                .showToast(context = context, longToast = false)
                        }
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(
                8.dp + it
            )
        )
    }
}