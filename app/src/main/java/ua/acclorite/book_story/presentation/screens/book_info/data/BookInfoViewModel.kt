@file:Suppress("LABEL_NAME_CLASH")

package ua.acclorite.book_story.presentation.screens.book_info.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
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
import ua.acclorite.book_story.domain.model.BookWithText
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.use_case.book.CanResetCover
import ua.acclorite.book_story.domain.use_case.book.CheckForTextUpdate
import ua.acclorite.book_story.domain.use_case.book.DeleteBooks
import ua.acclorite.book_story.domain.use_case.book.GetBookById
import ua.acclorite.book_story.domain.use_case.book.ResetCoverImage
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.domain.use_case.book.UpdateBookWithText
import ua.acclorite.book_story.domain.use_case.book.UpdateCoverImageOfBook
import ua.acclorite.book_story.domain.use_case.history.InsertHistory
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.domain.util.UIViewModel
import ua.acclorite.book_story.presentation.core.navigation.Screen
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val updateBook: UpdateBook,
    private val updateBookWithText: UpdateBookWithText,
    private val updateCoverImageOfBook: UpdateCoverImageOfBook,
    private val insertHistory: InsertHistory,
    private val deleteBooks: DeleteBooks,
    private val getBookById: GetBookById,
    private val canResetCover: CanResetCover,
    private val resetCoverImage: ResetCoverImage,
    private val checkForTextUpdate: CheckForTextUpdate,
) : UIViewModel<BookInfoState, BookInfoEvent>() {

    companion object {
        @Composable
        fun getState() = getState<BookInfoViewModel, BookInfoState, BookInfoEvent>()

        @Composable
        fun getEvent() = getEvent<BookInfoViewModel, BookInfoState, BookInfoEvent>()
    }

    private val _state = MutableStateFlow(BookInfoState())
    override val state = _state.asStateFlow()

    private var eventJob = SupervisorJob()

    private var snackBarJob: Job? = null
    private var updateJob: Job? = null

    override fun onEvent(event: BookInfoEvent) {
        viewModelScope.launch(eventJob + Dispatchers.Main) {
            when (event) {
                is BookInfoEvent.OnInit -> init(event)

                is BookInfoEvent.OnClearViewModel -> clearViewModel()

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

                        updateBook.execute(book)
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
                        updateBook.execute(_state.value.book)

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

                is BookInfoEvent.OnCheckForTextUpdate -> {
                    updateJob?.cancel()
                    updateJob = launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                showConfirmTextUpdateDialog = false,
                                checkingForUpdate = true,
                                editTitle = false,
                                editAuthor = false,
                                editDescription = false
                            )
                        }

                        yield()

                        val result = checkForTextUpdate.execute(bookId = _state.value.book.id)
                        when (result) {
                            is Resource.Success -> {
                                if (result.data == null) {
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
                                            checkingForUpdate = false
                                        )
                                    }
                                    return@launch
                                } else {
                                    onEvent(
                                        BookInfoEvent.OnShowConfirmTextUpdateDialog(
                                            updatedText = result.data.first,
                                            updatedChapters = result.data.second
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            checkingForUpdate = false
                                        )
                                    }
                                    return@launch
                                }
                            }

                            is Resource.Error -> {
                                onEvent(
                                    BookInfoEvent.OnShowSnackbar(
                                        text = result.message?.asString(event.context) ?: "",
                                        action = event.context.getString(R.string.retry),
                                        onAction = {
                                            onEvent(
                                                BookInfoEvent.OnCheckForTextUpdate(
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
                                        checkingForUpdate = false
                                    )
                                }
                                return@launch
                            }
                        }
                    }
                }

                is BookInfoEvent.OnDismissConfirmTextUpdateDialog -> {
                    _state.update {
                        it.copy(
                            showConfirmTextUpdateDialog = false,
                            updatedText = null,
                            updatedChapters = null
                        )
                    }
                }

                is BookInfoEvent.OnShowConfirmTextUpdateDialog -> {
                    _state.update {
                        it.copy(
                            showConfirmTextUpdateDialog = true,
                            updatedText = event.updatedText,
                            updatedChapters = event.updatedChapters
                        )
                    }
                }

                is BookInfoEvent.OnConfirmTextUpdate -> {
                    launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                updating = true,
                                showConfirmTextUpdateDialog = false
                            )
                        }

                        if (_state.value.updatedText == null || _state.value.updatedChapters == null) {
                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.error_something_went_wrong_with_file
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnCheckForTextUpdate(
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
                                    updating = false,
                                    updatedText = null,
                                    updatedChapters = null
                                )
                            }
                            return@launch
                        }

                        val updatedText = _state.value.updatedText ?: return@launch
                        val updatedChapters = _state.value.updatedChapters ?: return@launch

                        updateBookWithText.execute(
                            BookWithText(
                                book = _state.value.book.copy(
                                    scrollIndex = (updatedText.size * _state.value.book.progress).roundToInt(),
                                    scrollOffset = 0,
                                    chapters = updatedChapters
                                ),
                                text = updatedText
                            )
                        ).apply {
                            if (this) return@apply

                            onEvent(
                                BookInfoEvent.OnShowSnackbar(
                                    text = event.context.getString(
                                        R.string.error_something_went_wrong_with_file
                                    ),
                                    action = event.context.getString(R.string.retry),
                                    onAction = {
                                        onEvent(
                                            BookInfoEvent.OnCheckForTextUpdate(
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
                                    updating = false,
                                    updatedText = null,
                                    updatedChapters = null
                                )
                            }
                            return@launch
                        }

                        getBookById.execute(_state.value.book.id).apply {
                            if (this == null) {
                                onEvent(
                                    BookInfoEvent.OnShowSnackbar(
                                        text = event.context.getString(
                                            R.string.error_something_went_wrong_with_file
                                        ),
                                        action = event.context.getString(R.string.retry),
                                        onAction = {
                                            onEvent(
                                                BookInfoEvent.OnCheckForTextUpdate(
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
                                        updating = false,
                                        updatedText = null,
                                        updatedChapters = null
                                    )
                                }
                                return@launch
                            }

                            _state.update {
                                it.copy(
                                    book = this,
                                    updatedText = null,
                                    updatedChapters = null
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
                                    updating = false
                                )
                            }
                        }
                    }
                }

                is BookInfoEvent.OnCancelTextUpdate -> {
                    _state.update {
                        updateJob?.cancel()

                        it.copy(
                            showConfirmTextUpdateDialog = false,
                            checkingForUpdate = false,
                            updatedText = null,
                            updatedChapters = null
                        )
                    }
                }

                is BookInfoEvent.OnNavigateToReaderScreen -> {
                    launch {
                        onEvent(BookInfoEvent.OnCancelTextUpdate)
                        _state.value.book.id.let {
                            insertHistory.execute(
                                History(
                                    bookId = it,
                                    book = null,
                                    time = Date().time
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

                is BookInfoEvent.OnShowHideMoreBottomSheet -> {
                    _state.update {
                        it.copy(
                            showMoreBottomSheet = event.show
                        )
                    }
                }
            }
        }
    }

    private fun init(event: BookInfoEvent.OnInit) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = getBookById.execute(event.screen.bookId)

            if (book == null) {
                event.onNavigate {
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

            if (event.screen.startUpdate) {
                onEvent(
                    BookInfoEvent.OnCheckForTextUpdate(
                        snackbarState = event.snackbarState,
                        context = event.context
                    )
                )
                event.onNavigate {
                    putScreen(
                        Screen.BookInfo(
                            event.screen.bookId,
                            false
                        )
                    )
                }
            }
            onEvent(BookInfoEvent.OnCheckCoverReset)
        }
    }

    private fun clearViewModel() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                BookInfoState()
            }

            eventJob.cancel()
            eventJob.join()
            eventJob = SupervisorJob()
        }
    }

    private suspend fun clear() {
        eventJob.cancel()
        eventJob.join()
        eventJob = SupervisorJob()
    }
}