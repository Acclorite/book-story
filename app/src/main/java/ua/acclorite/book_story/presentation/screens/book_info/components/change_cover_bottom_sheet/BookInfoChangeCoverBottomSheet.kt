package ua.acclorite.book_story.presentation.screens.book_info.components.change_cover_bottom_sheet

import android.widget.Toast
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
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Change cover bottom sheet. Lets user select photo from the gallery and replaces old one.
 *
 * @param state [BookInfoState] instance.
 * @param onEvent [BookInfoEvent] callback.
 * @param onLibraryUpdateEvent [LibraryEvent] callback.
 * @param onHistoryUpdateEvent [HistoryEvent] callback.
 */
@Composable
fun BookInfoChangeCoverBottomSheet(
    state: State<BookInfoState>,
    onEvent: (BookInfoEvent) -> Unit,
    onLibraryUpdateEvent: (LibraryEvent.OnUpdateBook) -> Unit,
    onHistoryUpdateEvent: (HistoryEvent.OnUpdateBook) -> Unit
) {
    val context = LocalContext.current
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onEvent(
                    BookInfoEvent.OnChangeCover(
                        uri,
                        context,
                        refreshList = {
                            onLibraryUpdateEvent(LibraryEvent.OnUpdateBook(it))
                            onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
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

    LaunchedEffect(Unit) {
        onEvent(BookInfoEvent.OnCheckCoverReset)
    }

    CustomBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onEvent(BookInfoEvent.OnShowHideChangeCoverBottomSheet)
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxWidth()) {
            if (state.value.canResetCover) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.Restore,
                        text = stringResource(id = R.string.reset_cover),
                        description = stringResource(id = R.string.reset_cover_desc)
                    ) {
                        onEvent(
                            BookInfoEvent.OnResetCoverImage(
                                refreshList = {
                                    onLibraryUpdateEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
                                },
                                showResult = {
                                    Toast.makeText(
                                        context,
                                        it.asString(context),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        )
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

            if (state.value.book.coverImage != null) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.HideImage,
                        text = stringResource(id = R.string.delete_cover),
                        description = stringResource(id = R.string.delete_cover_desc)
                    ) {
                        onEvent(
                            BookInfoEvent.OnDeleteCover(
                                refreshList = {
                                    onLibraryUpdateEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
                                }
                            )
                        )
                        Toast.makeText(
                            context,
                            context.getString(R.string.cover_image_deleted),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(
                        8.dp + padding
                    )
                )
            }
        }
    }
}