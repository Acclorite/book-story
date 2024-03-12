package ua.acclorite.book_story.presentation.screens.browse.data

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.use_case.FastGetBooks
import ua.acclorite.book_story.domain.use_case.GetBooksFromFiles
import ua.acclorite.book_story.domain.use_case.GetFilesFromDownloads
import ua.acclorite.book_story.domain.use_case.InsertBooks
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.util.Resource
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getBooksFromFiles: GetBooksFromFiles,
    private val getFilesFromDownloads: GetFilesFromDownloads,
    private val insertBooks: InsertBooks,
    private val fastGetBooks: FastGetBooks
) : ViewModel() {

    private val _state = MutableStateFlow(BrowseState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            getFilesFromDownloads()
        }
    }

    fun onEvent(event: BrowseEvent) {
        when (event) {
            is BrowseEvent.OnLegacyStoragePermissionRequest -> {
                event.permissionState.launchPermissionRequest()

                viewModelScope.launch {
                    for (i in 0 until 100) {
                        if (!event.permissionState.status.isGranted) {
                            delay(100)
                            continue
                        }
                        _state.update {
                            it.copy(
                                requestPermissionDialog = false,
                                showErrorMessage = false
                            )
                        }
                        onEvent(BrowseEvent.OnRefreshList)
                        break
                    }
                }
            }

            is BrowseEvent.OnStoragePermissionRequest -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    val uri = Uri.parse("package:${event.activity.packageName}")
                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                    event.activity.startActivity(intent)

                    viewModelScope.launch {
                        for (i in 0 until 20) {
                            if (!Environment.isExternalStorageManager()) {
                                delay(1000)
                                continue
                            }
                            _state.update {
                                it.copy(
                                    requestPermissionDialog = false,
                                    showErrorMessage = false
                                )
                            }
                            onEvent(BrowseEvent.OnRefreshList)
                            break
                        }
                    }
                }
            }

            is BrowseEvent.OnStoragePermissionDismiss -> {
                val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                val isPermissionGranted =
                    if (!legacyPermission) Environment.isExternalStorageManager()
                    else event.permissionState.status.isGranted

                _state.update {
                    it.copy(
                        requestPermissionDialog = false,
                        showErrorMessage = !isPermissionGranted
                    )
                }

                if (isPermissionGranted) {
                    viewModelScope.launch(Dispatchers.IO) {
                        getFilesFromDownloads()
                    }
                }

            }

            is BrowseEvent.OnRefreshList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            hasSelectedItems = false,
                            showSearch = false
                        )
                    }

                    getFilesFromDownloads("")
                    delay(500)
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                }
            }

            is BrowseEvent.OnPermissionCheck -> {
                val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                val isPermissionGranted =
                    if (!legacyPermission) Environment.isExternalStorageManager()
                    else event.permissionState.status.isGranted

                if (isPermissionGranted) {
                    return
                }
                _state.update {
                    it.copy(
                        requestPermissionDialog = true,
                        showErrorMessage = false
                    )
                }
            }

            is BrowseEvent.OnSelectFile -> {
                val indexOfFile = _state.value.selectableFiles.indexOf(event.file)
                val editedList = _state.value.selectableFiles.toMutableList()
                editedList[indexOfFile] = editedList[indexOfFile].copy(
                    second = !editedList[indexOfFile].second
                )

                _state.update {
                    it.copy(
                        selectableFiles = editedList.toList(),
                        selectedItemsCount = editedList.filter { file -> file.second }.size,
                        hasSelectedItems = editedList.any { file -> file.second }
                    )
                }
            }

            is BrowseEvent.OnSelectBook -> {
                val indexOfFile = _state.value.selectedBooks.indexOf(event.book)
                val editedList = _state.value.selectedBooks.toMutableList()
                editedList[indexOfFile] = NullableBook.NotNull(
                    editedList[indexOfFile].book!!.copy(
                        second = !editedList[indexOfFile].book!!.second
                    )
                )

                if (!editedList.any { it.book?.second == true }) {
                    return
                }

                _state.update {
                    it.copy(
                        selectedBooks = editedList
                    )
                }
            }

            is BrowseEvent.OnSearchShowHide -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val shouldHide = _state.value.showSearch

                    if (shouldHide) {
                        getFilesFromDownloads("")
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
                            showSearch = !shouldHide
                        )
                    }
                }
            }

            is BrowseEvent.OnRequestFocus -> {
                if (!_state.value.hasFocused) {
                    event.focusRequester.requestFocus()
                    _state.update {
                        it.copy(
                            hasFocused = true
                        )
                    }
                }
            }

            is BrowseEvent.OnClearSelectedFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            selectableFiles = it.selectableFiles.map { file -> file.copy(second = false) },
                            hasSelectedItems = false
                        )
                    }
                }
            }

            is BrowseEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
                job?.cancel()
                job = viewModelScope.launch {
                    delay(500)
                    getFilesFromDownloads()
                }
            }

            is BrowseEvent.OnAddingDialogDismiss -> {
                _state.update {
                    it.copy(
                        showAddingDialog = false
                    )
                }
            }

            is BrowseEvent.OnAddingDialogRequest -> {
                _state.update {
                    it.copy(
                        showAddingDialog = true,
                        selectedBooks = emptyList()
                    )
                }
                onEvent(BrowseEvent.OnGetBooksFromFiles)
            }

            is BrowseEvent.OnGetBooksFromFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isBooksLoading = true
                        )
                    }

                    val books = getBooksFromFiles.execute(
                        _state.value.selectableFiles
                            .filter { it.second }
                            .map { it.first }
                    )

                    _state.update {
                        it.copy(
                            selectedBooks = books,
                            isBooksLoading = false
                        )
                    }
                }
            }

            is BrowseEvent.OnAddBooks -> {
                viewModelScope.launch {
                    val booksToInsert = _state.value.selectedBooks
                        .filterIsInstance<NullableBook.NotNull>()
                        .filter { it.book!!.second }
                        .map { it.book!!.first }

                    if (booksToInsert.isEmpty()) {
                        return@launch
                    }

                    insertBooks.execute(booksToInsert)
                    val books = fastGetBooks.execute("")

                    event.resetScroll()
                    _state.update {
                        it.copy(
                            showAddingDialog = false
                        )
                    }
                    onEvent(BrowseEvent.OnClearSelectedFiles)
                    onEvent(BrowseEvent.OnLoadList)

                    event.navigator.navigate(
                        Screen.LIBRARY,
                        false,
                        Argument("added_books", books)
                    )
                }
            }

            is BrowseEvent.OnLoadList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    getFilesFromDownloads()
                }
            }

            is BrowseEvent.OnUpdateScrollIndex -> {
                _state.update {
                    it.copy(
                        scrollIndex = event.index
                    )
                }
            }

            is BrowseEvent.OnUpdateScrollOffset -> {
                _state.update {
                    it.copy(
                        scrollOffset = event.offset
                    )
                }
            }
        }
    }

    private suspend fun getFilesFromDownloads(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        getFilesFromDownloads.execute(query).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            selectableFiles = result.data?.map { book -> Pair(book, false) }
                                ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = result.isLoading
                        )
                    }
                }

                is Resource.Error -> Unit
            }
        }
    }
}