@file:Suppress("LABEL_NAME_CLASH")

package ua.acclorite.book_story.presentation.screens.book_info.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.use_case.CanResetCover
import ua.acclorite.book_story.domain.use_case.DeleteBooks
import ua.acclorite.book_story.domain.use_case.GetBookById
import ua.acclorite.book_story.domain.use_case.GetBookFromFile
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.domain.use_case.ResetCoverImage
import ua.acclorite.book_story.domain.use_case.UpdateBookWithText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.use_case.UpdateCoverImageOfBook
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.Screen
import java.io.File
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val updateBookWithText: UpdateBookWithText,
    private val updateCoverImageOfBook: UpdateCoverImageOfBook,
    private val insertHistory: InsertHistory,
    private val deleteBooks: DeleteBooks,
    private val getBookFromFile: GetBookFromFile,
    private val getBookById: GetBookById,
    private val getText: GetText,
    private val canResetCover: CanResetCover,
    private val resetCoverImage: ResetCoverImage,
) : ViewModel() {

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    private var snackBarJob: Job? = null
    private var updateJob: Job? = null

    fun onEvent(event: BookInfoEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is BookInfoEvent.OnShowHideChangeCoverBottomSheet -> {
                    _state.update {
                        it.copy(
                            showChangeCoverBottomSheet = !it.showChangeCoverBottomSheet
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

                        val newCoverImage =
                            getBookById.execute(_state.value.book.id)?.coverImage ?: return@launch

                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    coverImage = newCoverImage
                                ),
                                showChangeCoverBottomSheet = false,
                                canResetCover = canResetCover.execute(bookId = it.book.id)
                            )
                        }
                        event.refreshList(_state.value.book)
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
                                showChangeCoverBottomSheet = false,
                                canResetCover = canResetCover.execute(bookId = it.book.id)
                            )
                        }
                        event.refreshList(_state.value.book)
                    }
                }

                is BookInfoEvent.OnCheckCoverReset -> {
                    launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                canResetCover = canResetCover.execute(_state.value.book.id)
                            )
                        }
                    }
                }

                is BookInfoEvent.OnResetCoverImage -> {
                    launch {
                        val result = resetCoverImage.execute(_state.value.book.id)

                        if (!result) {
                            event.showResult(UIText.StringResource(R.string.error_could_not_reset_cover))
                            return@launch
                        }

                        val book = getBookById.execute(_state.value.book.id)

                        if (book == null) {
                            event.showResult(UIText.StringResource(R.string.error_something_went_wrong))
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                book = book,
                                showChangeCoverBottomSheet = false,
                                canResetCover = false
                            )
                        }
                        event.showResult(UIText.StringResource(R.string.cover_reset))
                        event.refreshList(_state.value.book)
                    }
                }

                is BookInfoEvent.OnShowHideEditTitle -> {
                    launch(Dispatchers.IO) {
                        val shouldHide = _state.value.editTitle

                        if (!shouldHide) {
                            _state.update {
                                it.copy(
                                    titleValue = it.book.title,
                                    hasTitleFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editTitle = !shouldHide
                            )
                        }
                    }
                }

                is BookInfoEvent.OnTitleRequestFocus -> {
                    if (!_state.value.hasTitleFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasTitleFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnTitleValueChange -> {
                    _state.update {
                        it.copy(
                            titleValue = event.value
                        )
                    }
                }

                is BookInfoEvent.OnShowHideEditAuthor -> {
                    launch(Dispatchers.IO) {
                        val shouldHide = _state.value.editAuthor

                        if (!shouldHide) {
                            _state.update {
                                it.copy(
                                    authorValue = it.book.author.getAsString() ?: "",
                                    hasAuthorFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editAuthor = !shouldHide
                            )
                        }
                    }
                }

                is BookInfoEvent.OnAuthorRequestFocus -> {
                    if (!_state.value.hasAuthorFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasAuthorFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnAuthorValueChange -> {
                    _state.update {
                        it.copy(
                            authorValue = event.value
                        )
                    }
                }

                is BookInfoEvent.OnShowHideEditDescription -> {
                    launch(Dispatchers.IO) {
                        val shouldHide = _state.value.editDescription

                        if (!shouldHide) {
                            _state.update {
                                it.copy(
                                    descriptionValue = it.book.description ?: "",
                                    hasDescriptionFocused = false
                                )
                            }
                        }

                        _state.update {
                            it.copy(
                                editDescription = !shouldHide
                            )
                        }
                    }
                }

                is BookInfoEvent.OnDescriptionRequestFocus -> {
                    if (!_state.value.hasDescriptionFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasDescriptionFocused = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnDescriptionValueChange -> {
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
                                && description.isNotBlank()

                        val book = _state.value.book.copy(
                            title = if (titleChanged) title else _state.value.book.title,
                            author = if (authorChanged) UIText.StringValue(author)
                            else _state.value.book.author,
                            description = if (descriptionChanged) description
                            else _state.value.book.description
                        )

                        updateBooks.execute(listOf(book))
                        _state.update {
                            it.copy(
                                book = book
                            )
                        }
                        event.refreshList(_state.value.book)

                        if (state.value.editTitle) {
                            onEvent(BookInfoEvent.OnShowHideEditTitle)
                        }

                        if (state.value.editAuthor) {
                            onEvent(BookInfoEvent.OnShowHideEditAuthor)
                        }

                        if (state.value.editDescription) {
                            onEvent(BookInfoEvent.OnShowHideEditDescription)
                        }
                    }
                }

                is BookInfoEvent.OnShowHideDeleteDialog -> {
                    _state.update {
                        it.copy(
                            showDeleteDialog = !it.showDeleteDialog
                        )
                    }
                }

                is BookInfoEvent.OnDeleteBook -> {
                    launch {
                        _state.update {
                            it.copy(
                                showDeleteDialog = false
                            )
                        }

                        deleteBooks.execute(listOf(_state.value.book))
                        event.refreshList()

                        event.onNavigate {
                            navigateBack()
                        }
                    }
                }

                is BookInfoEvent.OnShowHideMoveDialog -> {
                    _state.update {
                        it.copy(
                            showMoveDialog = !it.showMoveDialog
                        )
                    }
                }

                is BookInfoEvent.OnSelectCategory -> {
                    _state.update {
                        it.copy(
                            selectedCategory = event.category
                        )
                    }
                }

                is BookInfoEvent.OnMoveBook -> {
                    launch {
                        _state.update {
                            it.copy(
                                showMoveDialog = false
                            )
                        }

                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    category = it.selectedCategory
                                )
                            )
                        }
                        updateBooks.execute(listOf(_state.value.book))

                        event.refreshList(_state.value.book)
                        event.updatePage(
                            Category.entries.dropLastWhile {
                                it != _state.value.selectedCategory
                            }.size - 1
                        )
                        event.onNavigate {
                            navigate(
                                Screen.Library,
                                useBackAnimation = true
                            )
                        }
                    }
                }

                is BookInfoEvent.OnShowHideDetailsBottomSheet -> {
                    _state.update {
                        it.copy(
                            showDetailsBottomSheet = !it.showDetailsBottomSheet
                        )
                    }
                }

                is BookInfoEvent.OnCopyToClipboard -> {
                    val clipboardManager =
                        event.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, event.text))

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        event.success()
                    }
                }

                is BookInfoEvent.OnShowSnackbar -> {
                    launch {
                        snackBarJob?.cancel()
                        event.snackbarState.currentSnackbarData?.dismiss()

                        if (event.durationMillis > 0) {
                            snackBarJob = launch(Dispatchers.IO) {
                                yield()
                                delay(event.durationMillis)
                                yield()
                                event.snackbarState.currentSnackbarData?.dismiss()
                            }
                        }

                        val snackbar = event.snackbarState.showSnackbar(
                            event.text,
                            actionLabel = event.action
                        )

                        when (snackbar) {
                            SnackbarResult.Dismissed -> Unit
                            SnackbarResult.ActionPerformed -> {
                                event.onAction()
                            }
                        }
                    }
                }

                is BookInfoEvent.OnLoadUpdate -> {
                    updateJob?.cancel()
                    updateJob = launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                showConfirmUpdateDialog = false,
                                isLoadingUpdate = true,
                                editTitle = false,
                                editAuthor = false,
                                editDescription = false
                            )
                        }

                        yield()
                        if (!File(_state.value.book.filePath).exists()) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.file_not_found,
                                        _state.value.book.filePath
                                            .substringAfterLast("/")
                                            .takeLast(25)
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnLoadUpdate(
                                                snackbarState = event.snackbarState,
                                                context = event.context
                                            )
                                        )
                                    },
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )
                            delay(500)
                            _state.update {
                                it.copy(
                                    isLoadingUpdate = false
                                )
                            }
                            return@launch
                        }

                        yield()
                        val updatedBook = getBookFromFile.execute(File(_state.value.book.filePath))
                        yield()

                        if (updatedBook is NullableBook.Null) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = updatedBook.message?.asString(event.context)
                                        ?: event.context.getString(R.string.error_something_went_wrong_with_file),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnLoadUpdate(
                                                snackbarState = event.snackbarState,
                                                context = event.context
                                            )
                                        )
                                    },
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )

                            delay(500)
                            _state.update {
                                it.copy(
                                    isLoadingUpdate = false
                                )
                            }
                            return@launch
                        }
                        yield()

                        val book = _state.value.book

                        var textUpdated = false

                        val updatedText = updatedBook.text
                        val text = getText.execute(book.textPath)

                        if (updatedText != text) {
                            textUpdated = true
                        }

                        yield()
                        if (!textUpdated) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    event.context.getString(R.string.nothing_changed),
                                    action = null,
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )
                            delay(500)
                            _state.update {
                                it.copy(
                                    isLoadingUpdate = false
                                )
                            }
                            return@launch
                        }

                        yield()
                        onEvent(
                            BookInfoEvent.OnShowConfirmUpdateDialog(
                                updatedText = updatedBook.text,
                            )
                        )

                        _state.update {
                            it.copy(
                                isLoadingUpdate = false
                            )
                        }
                    }
                }

                is BookInfoEvent.OnDismissConfirmUpdateDialog -> {
                    _state.update {
                        it.copy(
                            showConfirmUpdateDialog = false,
                            updatedText = null
                        )
                    }
                }

                is BookInfoEvent.OnShowConfirmUpdateDialog -> {
                    _state.update {
                        it.copy(
                            showConfirmUpdateDialog = true,
                            updatedText = event.updatedText
                        )
                    }
                }

                is BookInfoEvent.OnConfirmUpdate -> {
                    launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                isRefreshing = true,
                                showConfirmUpdateDialog = false
                            )
                        }

                        if (_state.value.updatedText == null) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.error_something_went_wrong_with_file
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnLoadUpdate(
                                                snackbarState = event.snackbarState,
                                                context = event.context
                                            )
                                        )
                                    },
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )
                            delay(500)
                            _state.update {
                                it.copy(
                                    isRefreshing = false
                                )
                            }
                            return@launch
                        }

                        val updatedText = _state.value.updatedText ?: return@launch
                        val isSuccess = updateBookWithText.execute(
                            book = _state.value.book,
                            text = updatedText
                        )

                        if (!isSuccess) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.error_something_went_wrong_with_file
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnLoadUpdate(
                                                snackbarState = event.snackbarState,
                                                context = event.context
                                            )
                                        )
                                    },
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )
                            delay(500)
                            _state.update {
                                it.copy(
                                    isRefreshing = false
                                )
                            }
                            return@launch
                        }

                        val newBook = getBookById.execute(_state.value.book.id)
                        if (newBook == null) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.error_something_went_wrong_with_file
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnLoadUpdate(
                                                snackbarState = event.snackbarState,
                                                context = event.context
                                            )
                                        )
                                    },
                                    durationMillis = 4000L,
                                    snackbarState = event.snackbarState
                                )
                            )
                            delay(500)
                            _state.update {
                                it.copy(
                                    isRefreshing = false
                                )
                            }
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                book = newBook,
                                updatedText = null
                            )
                        }
                        event.refreshList(_state.value.book)

                        onEvent(
                            BookInfoEvent.OnShowSnackbar(
                                event.context.getString(R.string.book_updated),
                                action = null,
                                durationMillis = 4000L,
                                snackbarState = event.snackbarState
                            )
                        )

                        delay(500)
                        _state.update {
                            it.copy(
                                isRefreshing = false
                            )
                        }
                    }
                }

                is BookInfoEvent.OnCancelUpdate -> {
                    _state.update {
                        updateJob?.cancel()

                        it.copy(
                            showConfirmUpdateDialog = false,
                            isLoadingUpdate = false
                        )
                    }
                }

                is BookInfoEvent.OnNavigateToReaderScreen -> {
                    launch {
                        onEvent(BookInfoEvent.OnCancelUpdate)
                        _state.value.book.id.let {
                            insertHistory.execute(
                                listOf(
                                    History(
                                        bookId = it,
                                        book = null,
                                        time = Date().time
                                    )
                                )
                            )
                        }
                        event.onNavigate {
                            navigate(
                                Screen.Reader(_state.value.book.id)
                            )
                        }
                    }
                }
            }
        }
    }

    fun init(screen: Screen.BookInfo, onNavigate: OnNavigate) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(screen.bookId)

            if (book == null) {
                onNavigate {
                    navigateBack()
                }
                return@launch
            }

            _state.update {
                BookInfoState(
                    book = book
                )
            }
            clear()

            onEvent(BookInfoEvent.OnCheckCoverReset)
        }
    }

    private suspend fun clear() {
        eventJob.cancel()
        eventJob.join()
        eventJob = SupervisorJob()
    }

    fun clearViewModel() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                BookInfoState()
            }

            eventJob.cancel()
            eventJob.join()
            eventJob = SupervisorJob()
        }
    }
}