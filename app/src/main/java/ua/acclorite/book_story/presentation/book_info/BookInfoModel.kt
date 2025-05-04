/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import ua.acclorite.book_story.domain.use_case.book.CanResetCover
import ua.acclorite.book_story.domain.use_case.book.DeleteBooks
import ua.acclorite.book_story.domain.use_case.book.GetBookById
import ua.acclorite.book_story.domain.use_case.book.ResetCoverImage
import ua.acclorite.book_story.domain.use_case.book.UpdateBook
import ua.acclorite.book_story.domain.use_case.book.UpdateCoverImageOfBook
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.library.LibraryScreen
import ua.acclorite.book_story.ui.common.util.showToast
import javax.inject.Inject

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
                is BookInfoEvent.OnShowDetailsBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = BookInfoScreen.DETAILS_BOTTOM_SHEET
                        )
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

                is BookInfoEvent.OnDismissBottomSheet -> {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }

                is BookInfoEvent.OnShowTitleDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.TITLE_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionTitleDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    title = event.title
                                )
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.title_changed)
                                .showToast(context = event.context)
                        }
                    }
                }

                is BookInfoEvent.OnShowAuthorDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.AUTHOR_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionAuthorDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    author = event.author
                                )
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.author_changed)
                                .showToast(context = event.context)
                        }
                    }
                }

                is BookInfoEvent.OnShowDescriptionDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.DESCRIPTION_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionDescriptionDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    description = event.description
                                )
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.description_changed)
                                .showToast(context = event.context)
                        }
                    }
                }

                is BookInfoEvent.OnShowPathDialog -> {
                    _state.update {
                        it.copy(
                            dialog = BookInfoScreen.PATH_DIALOG
                        )
                    }
                }

                is BookInfoEvent.OnActionPathDialog -> {
                    launch {
                        _state.update {
                            it.copy(
                                book = it.book.copy(
                                    filePath = event.path
                                )
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.path_changed)
                                .showToast(context = event.context)
                        }
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
                                book = it.book.copy(
                                    categories = event.selectedCategories.map { it.id }
                                ),
                                dialog = null,
                                bottomSheet = null
                            )
                        }
                        updateBook.execute(_state.value.book)

                        LibraryScreen.refreshListChannel.trySend(0)
                        HistoryScreen.refreshListChannel.trySend(0)

                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.book_moved)
                                .showToast(context = event.context)
                        }
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
        changePath: Boolean,
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

            if (changePath) {
                onEvent(BookInfoEvent.OnShowPathDialog)
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