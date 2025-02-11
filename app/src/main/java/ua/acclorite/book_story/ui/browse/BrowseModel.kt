/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import android.os.Build
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.BrowseSortOrder
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.NullableBook
import ua.acclorite.book_story.domain.library.book.SelectableNullableBook
import ua.acclorite.book_story.domain.use_case.book.InsertBook
import ua.acclorite.book_story.domain.use_case.file_system.GetBookFromFile
import ua.acclorite.book_story.domain.use_case.file_system.GetFilesFromDevice
import ua.acclorite.book_story.domain.use_case.permission.GrantStoragePermission
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.library.LibraryScreen
import java.io.File
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class BrowseModel @Inject constructor(
    private val grantStoragePermission: GrantStoragePermission,
    private val getFilesFromDevice: GetFilesFromDevice,
    private val getBookFromFile: GetBookFromFile,
    private val insertBook: InsertBook
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(BrowseState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            onEvent(
                BrowseEvent.OnRefreshList(
                    loading = true,
                    hideSearch = true
                )
            )
        }

        /* Observe channel - - - - - - - - - - - */
        viewModelScope.launch(Dispatchers.IO) {
            BrowseScreen.refreshListChannel.receiveAsFlow().collectLatest {
                onEvent(
                    BrowseEvent.OnRefreshList(
                        loading = false,
                        hideSearch = false
                    )
                )
            }
        }
        /* - - - - - - - - - - - - - - - - - - - */
    }

    private var refreshJob: Job? = null
    private var changeSearchQueryJob: Job? = null
    private var storagePermissionJob: Job? = null
    private var getAddDialogBooksJob: Job? = null

    @OptIn(ExperimentalPermissionsApi::class)
    fun onEvent(event: BrowseEvent) {
        when (event) {
            is BrowseEvent.OnRefreshList -> {
                refreshJob?.cancel()
                refreshJob = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            isLoading = event.loading,
                            showSearch = if (event.hideSearch) false else it.showSearch
                        )
                    }

                    yield()
                    getFilesFromDownloads()

                    delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false
                        )
                    }
                }
            }

            is BrowseEvent.OnSearchVisibility -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!event.show) {
                        onEvent(
                            BrowseEvent.OnRefreshList(
                                loading = false,
                                hideSearch = true
                            )
                        )
                    } else {
                        _state.update {
                            it.copy(
                                searchQuery = "",
                                hasFocused = false
                            )
                        }
                    }

                    _state.update {
                        it.copy(
                            showSearch = event.show
                        )
                    }
                }
            }

            is BrowseEvent.OnRequestFocus -> {
                viewModelScope.launch(Dispatchers.Main) {
                    if (!_state.value.hasFocused) {
                        event.focusRequester.requestFocus()
                        _state.update {
                            it.copy(
                                hasFocused = true
                            )
                        }
                    }
                }
            }

            is BrowseEvent.OnSearchQueryChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            searchQuery = event.query
                        )
                    }
                    changeSearchQueryJob?.cancel()
                    changeSearchQueryJob = launch(Dispatchers.IO) {
                        delay(500)
                        yield()
                        onEvent(BrowseEvent.OnSearch)
                    }
                }
            }

            is BrowseEvent.OnSearch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(
                        BrowseEvent.OnRefreshList(
                            loading = false,
                            hideSearch = false
                        )
                    )
                }
            }

            is BrowseEvent.OnClearSelectedFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            files = it.files.map { it.copy(selected = false) },
                            hasSelectedItems = false
                        )
                    }
                }
            }

            is BrowseEvent.OnSelectFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.files.map { file ->
                        if (
                            event.files.any {
                                file.path.startsWith(it.path)
                            } && event.includedFileFormats.run {
                                if (isEmpty()) return@run true
                                any {
                                    file.path.endsWith(it, ignoreCase = true)
                                }
                            }
                        ) {
                            file.copy(selected = true)
                        } else {
                            file
                        }
                    }

                    _state.update {
                        it.copy(
                            files = editedList,
                            selectedItemsCount = editedList.filter { file ->
                                file.selected
                            }.size.run {
                                if (this == 0) return@run it.selectedItemsCount
                                this
                            },
                            hasSelectedItems = editedList.any { file ->
                                file.selected
                            }
                        )
                    }
                }
            }

            is BrowseEvent.OnSelectFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.files.map { file ->
                        if (event.file.path == file.path) {
                            file.copy(
                                selected = !file.selected
                            )
                        } else {
                            file
                        }
                    }

                    _state.update {
                        it.copy(
                            files = editedList,
                            selectedItemsCount = editedList.filter { file ->
                                file.selected
                            }.size.run {
                                if (this == 0) return@run it.selectedItemsCount
                                this
                            },
                            hasSelectedItems = editedList.any { file ->
                                file.selected
                            }
                        )
                    }
                }
            }

            is BrowseEvent.OnShowFilterBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            bottomSheet = BrowseScreen.FILTER_BOTTOM_SHEET
                        )
                    }
                }
            }

            is BrowseEvent.OnDismissBottomSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            bottomSheet = null
                        )
                    }
                }
            }

            is BrowseEvent.OnPermissionCheck -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    val isPermissionGranted = if (!legacyPermission) {
                        Environment.isExternalStorageManager()
                    } else event.storagePermissionState.status.isGranted

                    if (isPermissionGranted) {
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            dialog = BrowseScreen.PERMISSION_DIALOG,
                            isError = false
                        )
                    }
                }
            }

            is BrowseEvent.OnActionPermissionDialog -> {
                storagePermissionJob?.cancel()
                storagePermissionJob = viewModelScope.launch(Dispatchers.IO) {
                    grantStoragePermission.execute(
                        activity = event.activity,
                        storagePermissionState = event.storagePermissionState
                    ).apply {
                        if (this) {
                            _state.update {
                                it.copy(
                                    dialog = null,
                                    isError = false
                                )
                            }
                            onEvent(
                                BrowseEvent.OnRefreshList(
                                    loading = true,
                                    hideSearch = false
                                )
                            )
                        }
                    }
                }
            }

            is BrowseEvent.OnDismissPermissionDialog -> {
                viewModelScope.launch {
                    val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    val permissionGranted = if (!legacyPermission) {
                        Environment.isExternalStorageManager()
                    } else event.storagePermissionState.status.isGranted

                    storagePermissionJob?.cancel()
                    _state.update {
                        it.copy(
                            isError = !permissionGranted,
                            dialog = null
                        )
                    }

                    if (permissionGranted) {
                        viewModelScope.launch(Dispatchers.IO) {
                            onEvent(
                                BrowseEvent.OnRefreshList(
                                    loading = true,
                                    hideSearch = false
                                )
                            )
                        }
                    }
                }
            }

            is BrowseEvent.OnShowAddDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = BrowseScreen.ADD_DIALOG,
                            selectedBooksAddDialog = emptyList()
                        )
                    }

                    getAddDialogBooksJob = launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                loadingAddDialog = true
                            )
                        }

                        yield()

                        val books = mutableListOf<NullableBook>()
                        _state.value.files
                            .filter { it.selected }
                            .ifEmpty {
                                _state.update {
                                    it.copy(
                                        loadingAddDialog = false,
                                        dialog = null
                                    )
                                }
                                return@launch
                            }
                            .map { File(it.path) }
                            .forEach {
                                yield()
                                books.add(getBookFromFile.execute(it))
                            }

                        yield()

                        _state.update {
                            it.copy(
                                selectedBooksAddDialog = books.map { book ->
                                    SelectableNullableBook(
                                        data = book,
                                        selected = true
                                    )
                                },
                                loadingAddDialog = false
                            )
                        }
                    }
                }
            }

            is BrowseEvent.OnDismissAddDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                    getAddDialogBooksJob?.cancel()
                }
            }

            is BrowseEvent.OnActionAddDialog -> {
                viewModelScope.launch {
                    val booksToInsert = _state.value.selectedBooksAddDialog.mapNotNull {
                        if (it.data is NullableBook.NotNull && it.selected) {
                            return@mapNotNull it.data
                        }
                        return@mapNotNull null
                    }.ifEmpty { return@launch }

                    var failed = false
                    for (book in booksToInsert) {
                        if (!insertBook.execute(book.bookWithCover!!)) {
                            failed = true
                            break
                        }
                    }

                    if (failed) {
                        _state.update {
                            it.copy(
                                dialog = null
                            )
                        }
                        onEvent(
                            BrowseEvent.OnRefreshList(
                                loading = false,
                                hideSearch = false
                            )
                        )
                        onEvent(BrowseEvent.OnClearSelectedFiles)

                        withContext(Dispatchers.Main) {
                            event.context
                                .getString(R.string.error_something_went_wrong)
                                .showToast(context = event.context)
                        }

                        return@launch
                    }

                    LibraryScreen.refreshListChannel.trySend(0)
                    LibraryScreen.scrollToPageCompositionChannel.trySend(0)

                    event.navigateToLibrary()
                    withContext(Dispatchers.Main) {
                        event.context
                            .getString(R.string.books_added)
                            .showToast(context = event.context)
                    }

                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                    onEvent(
                        BrowseEvent.OnRefreshList(
                            loading = false,
                            hideSearch = false
                        )
                    )
                    onEvent(BrowseEvent.OnClearSelectedFiles)
                }
            }

            is BrowseEvent.OnSelectAddDialog -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val index = _state.value.selectedBooksAddDialog.indexOf(event.book).apply {
                        if (this == -1) {
                            return@launch
                        }
                    }

                    val editedList = _state.value.selectedBooksAddDialog.toMutableList()
                    editedList[index] = editedList[index].run { copy(selected = !selected) }

                    if (!editedList.filter { it.data is NullableBook.NotNull }
                            .any { it.selected }) {
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            selectedBooksAddDialog = editedList
                        )
                    }
                }
            }

            is BrowseEvent.OnDismissDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dialog = null
                        )
                    }
                }
            }
        }
    }

    private suspend fun getFilesFromDownloads(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        getFilesFromDevice.execute(query).apply {
            yield()
            _state.update {
                it.copy(
                    files = this,
                    selectedItemsCount = 0,
                    hasSelectedItems = false,
                    isLoading = false
                )
            }
        }
    }

    fun resetScreen() {
        viewModelScope.launch {
            storagePermissionJob?.cancel()
            _state.update { it.copy(isError = false) }
        }
    }

    fun filterList(
        files: List<SelectableFile>,
        sortOrderDescending: Boolean,
        includedFilterItems: List<String>,
        sortOrder: BrowseSortOrder
    ): List<SelectableFile> {
        fun <T> compareByWithOrder(
            selector: (T) -> Comparable<*>?
        ): Comparator<T> {
            return if (sortOrderDescending) {
                compareByDescending(selector)
            } else {
                compareBy(selector)
            }
        }

        fun List<SelectableFile>.filterFiles(): List<SelectableFile> {
            if (includedFilterItems.isEmpty()) {
                return this
            }

            return filter { file ->
                includedFilterItems.any {
                    file.path.endsWith(
                        it, ignoreCase = true
                    )
                }
            }
        }

        return files
            .filterFiles()
            .sortedWith(
                compareByWithOrder<SelectableFile> {
                    when (sortOrder) {
                        BrowseSortOrder.NAME -> {
                            it.name.trim()
                        }

                        BrowseSortOrder.FILE_FORMAT -> {
                            it.path.substringAfterLast(".").lowercase().trimEnd()
                        }

                        BrowseSortOrder.FILE_SIZE -> {
                            it.size
                        }

                        else -> {
                            it.lastModified
                        }
                    }
                }
            )
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}