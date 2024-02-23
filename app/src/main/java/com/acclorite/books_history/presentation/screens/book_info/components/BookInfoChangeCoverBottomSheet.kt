package com.acclorite.books_history.presentation.screens.book_info.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoEvent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoViewModel
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel
import com.acclorite.books_history.ui.ElevationDefaults
import com.acclorite.books_history.ui.elevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoChangeCoverBottomSheet(
    libraryViewModel: LibraryViewModel,
    viewModel: BookInfoViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val book = state.book

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onEvent(
                    BookInfoEvent.OnChangeCover(
                        uri,
                        context,
                        refreshList = {
                            libraryViewModel.onEvent(LibraryEvent.OnLoadList)
                        }
                    )
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.cover_image_changed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onEvent(BookInfoEvent.OnShowHideChangeCoverBottomSheet)
        },
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.elevation(ElevationDefaults.BottomSheet)
    ) {

        BookInfoChangeCoverBottomSheetItem(
            icon = Icons.Default.ImageSearch,
            text = stringResource(id = R.string.change_cover),
            description = stringResource(id = R.string.change_cover_desc)
        ) {
            photoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        if (book.coverImage != null) {
            BookInfoChangeCoverBottomSheetItem(
                icon = Icons.Default.HideImage,
                text = stringResource(id = R.string.delete_cover),
                description = stringResource(id = R.string.delete_cover_desc)
            ) {
                viewModel.onEvent(BookInfoEvent.OnDeleteCover(
                    refreshList = {
                        libraryViewModel.onEvent(LibraryEvent.OnLoadList)
                    }
                ))
                Toast.makeText(
                    context,
                    context.getString(R.string.cover_image_deleted),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}