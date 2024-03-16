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
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.use_case.DeleteBooks
import ua.acclorite.book_story.domain.use_case.GetBookFromFile
import ua.acclorite.book_story.domain.use_case.GetBooksById
import ua.acclorite.book_story.domain.use_case.UpdateBooks
import ua.acclorite.book_story.domain.use_case.UpdateBooksWithText
import ua.acclorite.book_story.domain.use_case.UpdateCoverImageOfBook
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import javax.inject.Inject


@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val updateBooks: UpdateBooks,
    private val updateBooksWithText: UpdateBooksWithText,
    private val updateCoverImageOfBook: UpdateCoverImageOfBook,
    private val deleteBooks: DeleteBooks,
    private val getBookFromFile: GetBookFromFile,
    private val getBookById: GetBooksById
) : ViewModel() {

    private val _state = MutableStateFlow(BookInfoState())
    val state = _state.asStateFlow()

    private var job: Job? = null

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
                    event.refreshList()
                    val newCoverImage = getBookById.execute(
                        listOf(
                            _state.value.book.id ?: return@launch
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
                    event.refreshList()

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                coverImage = null
                            ),
                            showChangeCoverBottomSheet = false
                        )
                    }
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
                    event.refreshList()

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                title = title
                            )
                        )
                    }
                    onEvent(BookInfoEvent.OnShowHideEditTitle)
                }
            }

            is BookInfoEvent.OnShowHideMoreDropDown -> {
                _state.update {
                    it.copy(
                        showMoreDropDown = !it.showMoreDropDown
                    )
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

                    val book = _state.value.book.copy(
                        category = _state.value.selectedCategory
                    )
                    updateBooks.execute(listOf(book))

                    event.refreshList()

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
                            delay(event.durationMillis)
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

            is BookInfoEvent.OnUpdateBook -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            editTitle = false
                        )
                    }

                    if (_state.value.book.file == null) {
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
                                        BookInfoEvent.OnUpdateBook(
                                            refreshList = event.refreshList,
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

                    val nullableBook = getBookFromFile.execute(_state.value.book.file!!)

                    if (nullableBook is NullableBook.Null) {
                        onEvent(
                            BookInfoEvent.OnShowSnackbar(
                                text = nullableBook.message?.asString(event.context)
                                    ?: event.context.getString(R.string.error_something_went_wrong),
                                action = event.context.getString(R.string.retry),
                                onAction = {
                                    onEvent(
                                        BookInfoEvent.OnUpdateBook(
                                            refreshList = event.refreshList,
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

                    val updatedBook = nullableBook.book?.first ?: return@launch

                    _state.update {
                        it.copy(
                            book = it.book.copy(
                                author = updatedBook.author,
                                description = updatedBook.description
                            )
                        )
                    }
                    updateBooksWithText.execute(
                        listOf(
                            _state.value.book.copy(
                                author = updatedBook.author,
                                description = updatedBook.description,
                                text = updatedBook.text
                            )
                        )
                    )
                    event.refreshList()

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
        }
    }

    fun init(navigator: Navigator) {
        viewModelScope.launch {
            val bookIndex = navigator.retrieveArgument("book") as? Int

            if (bookIndex == null || bookIndex < 0) {
                navigator.navigateBack()
                return@launch
            }

            val book = getBookById.execute(listOf(bookIndex)).first()

            _state.update {
                BookInfoState(book = book)
            }
        }
    }
}