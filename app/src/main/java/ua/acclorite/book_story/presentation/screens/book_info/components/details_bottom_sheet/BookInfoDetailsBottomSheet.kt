package ua.acclorite.book_story.presentation.screens.book_info.components.details_bottom_sheet

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.ui.ElevationDefaults
import ua.acclorite.book_story.presentation.ui.elevation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Details bottom sheet. Displays name, path, last opened and size of the file.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoDetailsBottomSheet(
    viewModel: BookInfoViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val navigationBarPadding =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val pattern = remember {
        SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
    }
    val lastOpened = remember {
        pattern.format(Date(state.book.lastOpened ?: 0))
    }

    val sizeBytes = remember {
        state.book.file?.length() ?: 0
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
            viewModel.onEvent(BookInfoEvent.OnShowHideDetailsBottomSheet)
        },
        sheetState = rememberModalBottomSheetState(true),
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.elevation(ElevationDefaults.BottomSheet)
    ) {
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_name),
            description = state.book.filePath.substringAfterLast("/").trim()
        ) {
            viewModel.onEvent(BookInfoEvent.OnCopyToClipboard(
                context,
                state.book.filePath.substringAfterLast("/").trim(),
                success = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.copied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_path),
            description = state.book.filePath.trim()
        ) {
            viewModel.onEvent(BookInfoEvent.OnCopyToClipboard(
                context,
                state.book.filePath.trim(),
                success = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.copied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_last_opened),
            description = if (state.book.lastOpened != null) lastOpened
            else stringResource(id = R.string.never)
        ) {
            if (state.book.lastOpened != null) {
                viewModel.onEvent(BookInfoEvent.OnCopyToClipboard(
                    context,
                    lastOpened,
                    success = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ))
            }
        }
        BookInfoDetailsBottomSheetItem(
            title = stringResource(id = R.string.file_size),
            description = fileSize.ifBlank { stringResource(id = R.string.unknown) }
        ) {
            if (fileSize.isNotBlank()) {
                viewModel.onEvent(BookInfoEvent.OnCopyToClipboard(
                    context,
                    fileSize,
                    success = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ))
            }
        }

        Spacer(
            modifier = Modifier.height(
                8.dp + navigationBarPadding
            )
        )
    }
}