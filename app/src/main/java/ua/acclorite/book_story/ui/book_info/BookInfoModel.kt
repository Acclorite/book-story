package ua.acclorite.book_story.ui.book_info

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.domain.use_case.book.CanResetCover
import ua.acclorite.book_story.domain.use_case.book.DeleteBooks
import ua.acclorite.book_story.domain.use_case.book.GetBookById
import ua.acclorite.book_story.domain.use_case.book.ResetCoverImage
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.domain.use_case.book.UpdateCoverImageOfBook
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.browse.BrowseScreen
import ua.acclorite.book_story.ui.history.HistoryScreen
import ua.acclorite.book_story.ui.library.LibraryScreen
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class BookInfoModel @Inject constructor(
    private val getBookById: GetBookById,
    private val updateBook: UpdateBook,
    private val canResetCover: CanResetCover,
    private val updateCoverImageOfBook: UpdateCoverImageOfBook,
    private val resetCoverImage: ResetCoverImage,
    private val deleteBooks: DeleteBooks
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()
    private var resetJob: Job? = null

    fun onEvent(event: BookInfoEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is BookInfoEvent.OnCopyToClipboard -> {
                    val clipboardManager =
                        event.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, event.text))

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.copied)
                                .showToast(context = event.context, longToast = false)
                        }
                    }
                }

                is BookInfoEvent.OnEditTitleMode -> {
                    launch(Dispatchers.IO) {
                        if (event.edit) {
                            _state.update {
                                it.copy(
                                    titleValue = it.book.title,
                                    hasTitleFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editTitle = event.edit
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditTitleRequestFocus -> {
                    if (!_state.value.hasTitleFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasTitleFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditTitleValueChange -> {
                    _state.update {
                        it.copy(
                            titleValue = event.value
                        )
                    }
                }

                is BookInfoEvent.OnEditAuthorMode -> {
                    launch(Dispatchers.IO) {
                        if (event.edit) {
                            _state.update {
                                it.copy(
                                    authorValue = it.book.author.getAsString() ?: "",
                                    hasAuthorFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editAuthor = event.edit
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditAuthorRequestFocus -> {
                    if (!_state.value.hasAuthorFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasAuthorFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditAuthorValueChange -> {
                    _state.update {
                        it.copy(
                            authorValue = event.value
                        )
                    }
                }

                is BookInfoEvent.OnEditDescriptionMode -> {
                    launch(Dispatchers.IO) {
                        if (event.edit) {
                            _state.update {
                                it.copy(
                                    descriptionValue = it.book.description ?: "",
                                    hasDescriptionFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editDescription = event.edit
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditDescriptionRequestFocus -> {
                    if (!_state.value.hasDescriptionFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasDescriptionFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnEditDescriptionValueChange -> {
                    _state.update {
                        it.copy(
                            descriptionValue = event.value
                        )
                    }
                }

                is BookInfoEvent.OnUpdateData -> {
                    launch {
                        val title = _state.value.titleValue.trim().replace("\n", "")
                        val author = _state.value.authorValue.trim().replace("\n", "")
                        val description = _state.value.descriptionValue.trim().replace("\n", "")

                        val titleChanged = title != _state.value.book.title
                                && _state.value.editTitle
                                && title.isNotBlank()
                        val authorChanged = author != _state.value.book.author.getAsString()
                                && _state.value.editAuthor
                                && author.isNotBlank()
                        val descriptionChanged = description != _state.value.book.description
                                && _state.value.editDescription

                        val book = _state.value.book.copy(
                            title = if (titleChanged) title else _state.value.book.title,
                            author = if (authorChanged) UIText.StringValue(author)
                            else _state.value.book.author,
                            description = if (descriptionChanged) description
                            else _state.value.book.description
                        )

                        updateBook.execute(book)
                        _state.update {
                            it.copy(
                                book = book
                            )
                        }

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        if (_state.value.editTitle) {
                            onEvent(BookInfoEvent.OnEditTitleMode(false))
                        }

                        if (_state.value.editAuthor) {
                            onEvent(BookInfoEvent.OnEditAuthorMode(false))
                        }

                        if (_state.value.editDescription) {
                            onEvent(BookInfoEvent.OnEditDescriptionMode(false))
                        }
                    }
                }

                is BookInfoEvent.OnShowChangeCoverBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = BookInfoScreen.CHANGE_COVER_BOTTOM_SHEET
                        )
                    }
                }

                is BookInfoEvent.OnChangeCover -> {
                    launch {
                        val image = event.context.contentResolver?.openInputStream(event.uri)?.use {
                            BitmapFactory.decodeStream(it)
                        } ?: return@launch

                        updateCoverImageOfBook.execute(
                            _state.value.book,
                            image
                        )

                        val newCoverImage = getBookById.execute(
                            _state.value.book.id
                        )?.coverImage ?: return@launch

                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    coverImage = newCoverImage
                                ),
                                bottomSheet = null,
                                canResetCover = canResetCover.execute(bookId = it.book.id)
                            )
                        }

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.cover_image_changed)
                                .showToast(context = event.context)
                        }
                    }
                }

                is BookInfoEvent.OnResetCover -> {
                    launch {
                        val result = resetCoverImage.execute(_state.value.book.id)

                        if (!result) {
                            withContext(Dispatchers.Main) {
                                event.context.getString(R.string.error_could_not_reset_cover)
                                    .showToast(context = event.context)
                            }
                            return@launch
                        }

                        val book = getBookById.execute(_state.value.book.id)

                        if (book == null) {
                            withContext(Dispatchers.Main) {
                                event.context.getString(R.string.error_something_went_wrong)
                                    .showToast(context = event.context)
                            }
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                book = book,
                                bottomSheet = null,
                                canResetCover = false
                            )
                        }

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.cover_reset)
                                .showToast(context = event.context)
                        }

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)
                    }
                }

                is BookInfoEvent.OnDeleteCover -> {
                    launch {
                        if (_state.value.book.coverImage == null) {
                            return@launch
                        }

                        updateCoverImageOfBook.execute(
                            bookWithOldCover = _state.value.book,
                            newCoverImage = null
                        )
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    coverImage = null
                                ),
                                bottomSheet = null,
                                canResetCover = canResetCover.execute(bookId = it.book.id)
                            )
                        }

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.cover_image_deleted)
                                .showToast(context = event.context)
                        }
                    }
                }

                is BookInfoEvent.OnCheckCoverReset -> {
                    launch(Dispatchers.IO) {
                        if (_state.value.book.id == -1) return@launch
                        canResetCover.execute(_state.value.book.id).apply {
                            _state.update {
                                it.copy(
                                    canResetCover = this
                                )
                            }
                        }
                    }
                }

                is BookInfoEvent.OnShowMoreBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = BookInfoScreen.MORE_BOTTOM_SHEET
                        )
                    }
                }

                is BookInfoEvent.OnShowDetailsBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = BookInfoScreen.DETAILS_BOTTOM_SHEET
                        )
                    }
                }

                is BookInfoEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is BookInfoEvent.OnShowDeleteDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.DELETE_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionDeleteDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                dialog = null,
                                bottomSheet = null
                            )
                        }

                        deleteBooks.execute(listOf(_state.value.book))

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)
                        BrowseScreen.refreshListChannel.trySend(Unit)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.book_deleted)
                                .showToast(context = event.context)
                        }

                        event.navigateBack()
                    }
                }

                is BookInfoEvent.OnShowMoveDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.MOVE_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionMoveDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                dialog = null,
                                bottomSheet = null
                            )
                        }

                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    category = event.category
                                )
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        LibraryScreen.scrollToPageCompositionChannel.trySend(
                            Category.entries.dropLastWhile {
                                it != event.category
                            }.size - 1
                        )
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.book_moved)
                                .showToast(context = event.context)
                        }

                        event.navigateToLibrary()
                    }
                }

                is BookInfoEvent.OnDismissDialog -> {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }
            }
        }
    }

    fun init(
        bookId: Int,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(bookId)

            if (book == null) {
                navigateBack()
                return@launch
            }

            eventJob.cancel()
            resetJob?.cancel()
            eventJob.join()
            resetJob?.join()
            eventJob = SupervisorJob()

            _state.update {
                BookInfoState(
                    book = book
                )
            }

            onEvent(BookInfoEvent.OnCheckCoverReset)
        }
    }

    fun resetScreen() {
        resetJob = viewModelScope.launch(Dispatchers.Main) {
            eventJob.cancel()
            eventJob = SupervisorJob()

            yield()
            _state.update { BookInfoState() }
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}