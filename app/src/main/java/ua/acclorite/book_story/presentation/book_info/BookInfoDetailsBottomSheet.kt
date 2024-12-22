package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import ua.acclorite.book_story.ui.book_info.BookInfoEvent
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookInfoDetailsBottomSheet(
    book: Book,
    copyToClipboard: (BookInfoEvent.OnCopyToClipboard) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit
) {
    val context = LocalContext.current

    val pattern = remember {
        SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
    }
    val lastOpened = remember {
        pattern.format(Date(book.lastOpened ?: 0))
    }

    val sizeBytes = remember {
        val file = File(book.filePath)
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
                BookInfoDetailsBottomSheetItem(
                    title = stringResource(id = R.string.file_name),
                    description = book.filePath.substringAfterLast("/").trim()
                ) {
                    copyToClipboard(
                        BookInfoEvent.OnCopyToClipboard(
                            text = book.filePath.substringAfterLast("/").trim(),
                            context = context
                        )
                    )
                }
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    title = stringResource(id = R.string.file_path),
                    description = book.filePath.trim()
                ) {
                    copyToClipboard(
                        BookInfoEvent.OnCopyToClipboard(
                            text = book.filePath.trim(),
                            context = context
                        )
                    )
                }
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    title = stringResource(id = R.string.file_last_opened),
                    description = if (book.lastOpened != null) lastOpened
                    else stringResource(id = R.string.never)
                ) {
                    if (book.lastOpened != null) {
                        copyToClipboard(
                            BookInfoEvent.OnCopyToClipboard(
                                text = lastOpened,
                                context = context
                            )
                        )
                    }
                }
            }

            item {
                BookInfoDetailsBottomSheetItem(
                    title = stringResource(id = R.string.file_size),
                    description = fileSize.ifBlank { stringResource(id = R.string.unknown) }
                ) {
                    if (fileSize.isNotBlank()) {
                        copyToClipboard(
                            BookInfoEvent.OnCopyToClipboard(
                                text = fileSize,
                                context = context
                            )
                        )
                    }
                }
            }
        }
    }
}