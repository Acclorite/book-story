package ua.acclorite.book_story.presentation.screens.browse.data

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.use_case.GetBooksFromFiles
import ua.acclorite.book_story.domain.use_case.GetFilesFromDevice
import ua.acclorite.book_story.domain.use_case.InsertBook
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.presentation.data.Screen
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getBooksFromFiles: GetBooksFromFiles,
    private val getFilesFromDevice: GetFilesFromDevice,
    private val insertBook: InsertBook
) : ViewModel() {

    private val _state = MutableStateFlow(BrowseState())
    val state = _state.asStateFlow()

    private var job: Job? = null
    private var job2: Job? = null
    private var job3: Job? = null
    private var storagePermissionJob: Job? = null

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
            is BrowseEvent.OnStoragePermissionRequest -> {
                val legacyStoragePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

                val isPermissionGranted = if (legacyStoragePermission) {
                    event.storagePermissionState.status.isGranted
                } else {
                    Environment.isExternalStorageManager()
                }

                if (isPermissionGranted) {
                    _state.update {
                        it.copy(
                            requestPermissionDialog = false
                        )
                    }
                    event.hideErrorMessage()
                    onEvent(BrowseEvent.OnRefreshList)
                    return
                }

                if (legacyStoragePermission) {
                    if (!event.storagePermissionState.status.shouldShowRationale) {
                        event.storagePermissionState.launchPermissionRequest()
                    } else {
                        val uri = Uri.parse("package:${event.activity.packageName}")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)

                        if (intent.resolveActivity(event.activity.packageManager) != null) {
                            event.activity.startActivity(intent)
                        } else {
                            return
                        }
                    }
                }

                if (!legacyStoragePermission) {
                    val uri = Uri.parse("package:${event.activity.packageName}")
                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )

                    if (intent.resolveActivity(event.activity.packageManager) != null) {
                        event.activity.startActivity(intent)
                    } else {
                        return
                    }
                }

                storagePermissionJob?.cancel()
                storagePermissionJob = viewModelScope.launch {
                    while (true) {
                        val granted = if (legacyStoragePermission) {
                            event.storagePermissionState.status.isGranted
                        } else {
                            Environment.isExternalStorageManager()
                        }

                        if (!granted) {
                            delay(1000)
                            yield()
                            continue
                        }

                        yield()

                        _state.update {
                            it.copy(
                                requestPermissionDialog = false
                            )
                        }
                        event.hideErrorMessage()
                        onEvent(BrowseEvent.OnRefreshList)
                        break
                    }
                }
            }

            is BrowseEvent.OnStoragePermissionDismiss -> {
                val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                val isPermissionGranted =
                    if (!legacyPermission) Environment.isExternalStorageManager()
                    else event.permissionState.status.isGranted

                storagePermissionJob?.cancel()
                _state.update {
                    it.copy(
                        requestPermissionDialog = false
                    )
                }

                if (isPermissionGranted) {
                    viewModelScope.launch(Dispatchers.IO) {
                        getFilesFromDownloads()
                    }
                } else {
                    event.showErrorMessage()
                }
            }

            is BrowseEvent.OnRefreshList -> {
                job2?.cancel()
                job2 = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            hasSelectedItems = false,
                            showSearch = false,
                            listState = LazyListState(0, 0)
                        )
                    }
                    yield()
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
                viewModelScope.launch(Dispatchers.IO) {
                    val legacyPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    val isPermissionGranted =
                        if (!legacyPermission) Environment.isExternalStorageManager()
                        else event.permissionState.status.isGranted

                    if (isPermissionGranted) {
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            requestPermissionDialog = true
                        )
                    }
                    event.hideErrorMessage()
                }
            }

            is BrowseEvent.OnSelectFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.selectableFiles.map {
                        if (event.file.first.path == it.first.path) {
                            it.copy(
                                second = !it.second
                            )
                        } else {
                            it
                        }
                    }

                    _state.update {
                        it.copy(
                            selectableFiles = editedList,
                            selectedItemsCount = editedList.filter { file -> file.second }.size,
                            hasSelectedItems = editedList.any { file -> file.second }
                        )
                    }
                }
            }

            is BrowseEvent.OnSelectBook -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val indexOfFile = _state.value.selectedBooks.indexOf(event.book)

                    if (indexOfFile == -1) {
                        return@launch
                    }

                    val editedList = _state.value.selectedBooks.toMutableList()
                    editedList[indexOfFile] = editedList[indexOfFile].first to
                            !editedList[indexOfFile].second

                    if (!editedList.filter { it.first is NullableBook.NotNull }
                            .any { it.second }
                    ) {
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            selectedBooks = editedList
                        )
                    }
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
                job = viewModelScope.launch(Dispatchers.IO) {
                    delay(500)
                    yield()
                    onEvent(BrowseEvent.OnSearch)
                }
            }

            is BrowseEvent.OnSearch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getFilesFromDownloads()
                }
            }

            is BrowseEvent.OnAddingDialogDismiss -> {
                _state.update {
                    it.copy(
                        showAddingDialog = false
                    )
                }
                job3?.cancel(null)
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
                job3 = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isBooksLoading = true
                        )
                    }

                    yield()

                    val books = mutableListOf<NullableBook>()
                    _state.value.selectableFiles
                        .filter { it.second }
                        .map { it.first }
                        .forEach {
                            yield()
                            books.add(
                                getBooksFromFiles.execute(
                                    listOf(it)
                                ).first()
                            )
                        }

                    yield()

                    _state.update {
                        it.copy(
                            selectedBooks = books.map { book -> book to true },
                            isBooksLoading = false
                        )
                    }
                }
            }

            is BrowseEvent.OnAddBooks -> {
                viewModelScope.launch {
                    val booksToInsert = _state.value.selectedBooks
                        .filter { it.first is NullableBook.NotNull }
                        .filter { it.second }
                        .map { it.first }

                    if (booksToInsert.isEmpty()) {
                        return@launch
                    }

                    val failed = booksToInsert.any {
                        !insertBook.execute(
                            it.book!!,
                            it.coverImage,
                            it.text
                        )
                    }

                    if (failed) {
                        _state.update {
                            it.copy(
                                showAddingDialog = false
                            )
                        }
                        onEvent(BrowseEvent.OnLoadList)
                        onEvent(BrowseEvent.OnClearSelectedFiles)
                        event.onFailed()
                        return@launch
                    }

                    event.resetScroll()
                    event.onNavigate {
                        navigate(Screen.Library)
                    }
                    event.onSuccess()

                    _state.update {
                        it.copy(
                            showAddingDialog = false
                        )
                    }
                    onEvent(BrowseEvent.OnLoadList)
                    onEvent(BrowseEvent.OnClearSelectedFiles)
                }
            }

            is BrowseEvent.OnLoadList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            hasSelectedItems = false,
                            listState = LazyListState(0, 0)
                        )
                    }
                    getFilesFromDownloads()
                }
            }

            is BrowseEvent.OnUpdateScrollOffset -> {
                _state.update {
                    it.copy(
                        listState = LazyListState(
                            it.listState.firstVisibleItemIndex,
                            0
                        )
                    )
                }
            }
        }
    }

    private suspend fun getFilesFromDownloads(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        getFilesFromDevice.execute(query).collect { result ->
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

                is Resource.Error -> Unit
            }
        }
    }
}