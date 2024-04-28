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
import ua.acclorite.book_story.domain.use_case.DeleteBooks
import ua.acclorite.book_story.domain.use_case.GetBookFromFile
import ua.acclorite.book_story.domain.use_case.GetBooksById
import ua.acclorite.book_story.domain.use_case.GetText
import ua.acclorite.book_story.domain.use_case.InsertHistory
import ua.acclorite.book_story.domain.use_case.UpdateBookWithText
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.use_case.UpdateCoverImageOfBook
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Navigator
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
    private val getBookById: GetBooksById,
    private val getText: GetText
) : ViewModel() {

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    private var job: Job? = null
    private var job2: Job? = null

    fun onEvent(event: BookInfoEvent) {
        when (event) {
            is BookInfoEvent.OnShowHideChangeCoverBottomSheet -> {
                _state.update {
                    it.copy(
                        showChangeCoverBottomSheet = !it.showChangeCoverBottomSheet
                    )
                }
            }

            is BookInfoEvent.OnChangeCover -> {
                viewModelScope.launch {
                    val image = event.context.contentResolver?.openInputStream(event.uri)?.use {
                        BitmapFactory.decodeStream(it)
                    } ?: return@launch

                    updateCoverImageOfBook.execute(
                        _state.value.book,
                        image
                    )

                    val newCoverImage = getBookById.execute(
                        listOf(
                            _state.value.book.id
                        )
                    ).first().coverImage

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                coverImage = newCoverImage
                            ),
                            showChangeCoverBottomSheet = false
                        )
                    }
                    event.refreshList(_state.value.book)
                }
            }

            is BookInfoEvent.OnDeleteCover -> {
                viewModelScope.launch {
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
                            showChangeCoverBottomSheet = false
                        )
                    }
                    event.refreshList(_state.value.book)
                }
            }

            is BookInfoEvent.OnShowHideEditTitle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val shouldHide = _state.value.editTitle

                    if (!shouldHide) {
                        _state.update {
                            it.copy(
                                titleValue = it.book.title,
                                hasFocused = false
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

            is BookInfoEvent.OnRequestFocus -> {
                if (!_state.value.hasFocused) {
                    event.focusRequester.requestFocus()
                    _state.update {
                        it.copy(
                            hasFocused = true
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

            is BookInfoEvent.OnUpdateTitle -> {
                viewModelScope.launch {
                    val title = _state.value.titleValue.trim().replace("\n", "")

                    updateBooks.execute(
                        listOf(
                            _state.value.book.copy(title = title)
                        )
                    )
                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                title = title
                            )
                        )
                    }
                    event.refreshList(_state.value.book)

                    onEvent(BookInfoEvent.OnShowHideEditTitle)
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
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showDeleteDialog = false
                        )
                    }

                    deleteBooks.execute(listOf(_state.value.book))
                    event.refreshList()

                    event.navigator.navigateBack()
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
                viewModelScope.launch {
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
                    event.navigator.navigate(Screen.LIBRARY, true)
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
                viewModelScope.launch {
                    job?.cancel()
                    event.snackbarState.currentSnackbarData?.dismiss()

                    if (event.durationMillis > 0) {
                        job = viewModelScope.launch(Dispatchers.IO) {
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
                onEvent(BookInfoEvent.OnCancelUpdate)

                job2 = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isLoadingUpdate = true,
                            editTitle = false
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

                    var authorUpdated = false
                    var descriptionUpdated = false
                    var textUpdated = false

                    if (
                        updatedBook.book!!.author.asString(event.context) !=
                        book.author.asString(event.context)
                    ) {
                        authorUpdated = true
                    }
                    if (updatedBook.book.description != book.description) {
                        descriptionUpdated = true
                    }

                    val updatedText = updatedBook.text
                    val text = getText.execute(book.textPath)

                    if (updatedText.map { it.line } != text.map { it.line }) {
                        textUpdated = true
                    }

                    yield()
                    if (!authorUpdated && !descriptionUpdated && !textUpdated) {
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
                            updatedBook = updatedBook.book to updatedBook.text,
                            authorUpdated = authorUpdated,
                            descriptionUpdated = descriptionUpdated,
                            textUpdated = textUpdated
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
                        updatedBook = null,
                        authorChanged = false,
                        descriptionChanged = false,
                        textChanged = false
                    )
                }
            }

            is BookInfoEvent.OnShowConfirmUpdateDialog -> {
                _state.update {
                    it.copy(
                        showConfirmUpdateDialog = true,
                        updatedBook = event.updatedBook,
                        authorChanged = event.authorUpdated,
                        descriptionChanged = event.descriptionUpdated,
                        textChanged = event.textUpdated
                    )
                }
            }

            is BookInfoEvent.OnConfirmUpdate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            showConfirmUpdateDialog = false
                        )
                    }

                    if (_state.value.updatedBook == null) {
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

                    val book = _state.value.book
                    val updatedBook = _state.value.updatedBook ?: return@launch

                    val author = if (_state.value.authorChanged) {
                        updatedBook.first.author
                    } else {
                        book.author
                    }
                    val description = if (_state.value.descriptionChanged) {
                        updatedBook.first.description
                    } else {
                        book.description
                    }

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                author = author,
                                description = description
                            )
                        )
                    }

                    if (_state.value.textChanged) {
                        val isSuccess = updateBookWithText.execute(
                            book = _state.value.book,
                            text = updatedBook.second
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
                    } else {
                        updateBooks.execute(
                            listOf(
                                _state.value.book
                            )
                        )
                    }

                    if (_state.value.textChanged) {
                        val newBook = getBookById.execute(
                            listOf(_state.value.book.id)
                        ).first()

                        _state.update {
                            it.copy(
                                book = newBook,
                                authorChanged = false,
                                descriptionChanged = false,
                                textChanged = false,
                                updatedBook = null
                            )
                        }
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
                    job2?.cancel()
                    it.copy(
                        showConfirmUpdateDialog = false,
                        isLoadingUpdate = false
                    )
                }
            }

            is BookInfoEvent.OnNavigateToReaderScreen -> {
                viewModelScope.launch {
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
                    event.navigator.navigate(
                        Screen.READER,
                        false,
                        Argument(
                            "book", _state.value.book.id
                        )
                    )
                }
            }
        }
    }

    fun init(navigator: Navigator) {
        viewModelScope.launch {
            val bookId = navigator.retrieveArgument("book") as? Int

            if (bookId == null) {
                navigator.navigateBack()
                return@launch
            }

            val book = getBookById.execute(listOf(bookId))

            if (book.isEmpty()) {
                navigator.navigateBack()
                return@launch
            }

            _state.update {
                BookInfoState(book = book.first())
            }
        }
    }
}