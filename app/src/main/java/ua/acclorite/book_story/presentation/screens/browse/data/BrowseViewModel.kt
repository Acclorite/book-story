@file:Suppress("LABEL_NAME_CLASH")

package ua.acclorite.book_story.presentation.screens.browse.data

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.use_case.GetBookFromFile
import ua.acclorite.book_story.domain.use_case.GetFilesFromDevice
import ua.acclorite.book_story.domain.use_case.InsertBook
import ua.acclorite.book_story.domain.use_case.UpdateFavoriteDirectory
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.data.launchActivity
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseSortOrder
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getBookFromFile: GetBookFromFile,
    private val getFilesFromDevice: GetFilesFromDevice,
    private val insertBook: InsertBook,
    private val updateFavoriteDirectory: UpdateFavoriteDirectory,
) : ViewModel() {

    private val _state = MutableStateFlow(BrowseState())
    val state = _state.asStateFlow()

    private var searchQueryJob: Job? = null
    private var refreshListJob: Job? = null
    private var getBooksJob: Job? = null
    private var storagePermissionJob: Job? = null
    private var changeDirectoryJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    selectedDirectory = Environment.getExternalStorageDirectory(),
                    inNestedDirectory = false
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
                            requestPermissionDialog = false,
                            isError = false
                        )
                    }
                    onEvent(BrowseEvent.OnRefreshList)
                    return
                }

                if (legacyStoragePermission) {
                    if (!event.storagePermissionState.status.shouldShowRationale) {
                        event.storagePermissionState.launchPermissionRequest()
                    } else {
                        val uri = Uri.parse("package:${event.activity.packageName}")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)

                        var failure = false
                        intent.launchActivity(event.activity) {
                            failure = true
                        }
                        if (failure) {
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

                    var failure = false
                    intent.launchActivity(event.activity) {
                        failure = true
                    }
                    if (failure) {
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
                                requestPermissionDialog = false,
                                isError = false
                            )
                        }
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
                    _state.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
            }

            is BrowseEvent.OnRefreshList -> {
                refreshListJob?.cancel()
                refreshListJob = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isRefreshing = true,
                            hasSelectedItems = false,
                            showSearch = false,
                            listState = LazyListState(),
                            gridState = LazyGridState(),
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
                            requestPermissionDialog = true,
                            isError = false
                        )
                    }
                }
            }

            is BrowseEvent.OnSelectFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.selectableFiles.map { file ->
                        when (event.file.isDirectory) {
                            false -> {
                                if (event.file.fileOrDirectory.path == file.fileOrDirectory.path) {
                                    file.copy(
                                        isSelected = !file.isSelected
                                    )
                                } else {
                                    file
                                }
                            }

                            true -> {
                                if (
                                    file.fileOrDirectory.path.startsWith(
                                        event.file.fileOrDirectory.path
                                    ) && event.includedFileFormats.run {
                                        if (isEmpty()) return@run true
                                        any {
                                            file.fileOrDirectory.path.endsWith(
                                                it, ignoreCase = true
                                            ) || file.isDirectory
                                        }
                                    }
                                ) {
                                    file.copy(
                                        isSelected = !event.file.isSelected
                                    )
                                } else {
                                    file
                                }
                            }
                        }
                    }

                    _state.update {
                        it.copy(
                            selectableFiles = editedList,
                            selectedItemsCount = editedList.filter { file ->
                                file.isSelected && !file.isDirectory
                            }.size,
                            hasSelectedItems = editedList.any { file ->
                                file.isSelected && !file.isDirectory
                            }
                        )
                    }
                }
            }

            is BrowseEvent.OnSelectFiles -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val editedList = _state.value.selectableFiles.map { file ->
                        if (
                            event.files.any {
                                file.fileOrDirectory.path.startsWith(it.fileOrDirectory.path)
                            } && event.includedFileFormats.run {
                                if (isEmpty()) return@run true
                                any {
                                    file.fileOrDirectory.path.endsWith(
                                        it, ignoreCase = true
                                    ) || file.isDirectory
                                }
                            }
                        ) {
                            file.copy(isSelected = true)
                        } else {
                            file
                        }
                    }

                    _state.update {
                        it.copy(
                            selectableFiles = editedList,
                            selectedItemsCount = editedList.filter { file ->
                                file.isSelected && !file.isDirectory
                            }.size,
                            hasSelectedItems = editedList.any { file ->
                                file.isSelected && !file.isDirectory
                            }
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
                                hasSearched = false,
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
                            selectableFiles = it.selectableFiles.map { file ->
                                file.copy(
                                    isSelected = false
                                )
                            },
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
                searchQueryJob?.cancel()
                searchQueryJob = viewModelScope.launch(Dispatchers.IO) {
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
                getBooksJob?.cancel()
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
                getBooksJob = viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            isBooksLoading = true
                        )
                    }

                    yield()

                    val books = mutableListOf<NullableBook>()
                    _state.value.selectableFiles
                        .filter { it.isSelected && !it.isDirectory }
                        .map { it.fileOrDirectory }
                        .forEach {
                            yield()
                            books.add(getBookFromFile.execute(it))
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
                            listState = LazyListState(),
                            gridState = LazyGridState()
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
                        ),
                        gridState = LazyGridState(
                            it.gridState.firstVisibleItemIndex,
                            0
                        )
                    )
                }
            }

            is BrowseEvent.OnChangeDirectory -> {
                viewModelScope.launch {
                    changeDirectoryJob?.cancel()
                    searchQueryJob?.cancel()

                    changeDirectoryJob = launch(Dispatchers.IO) {
                        yield()
                        _state.update {
                            it.copy(
                                listState = LazyListState(),
                                gridState = LazyGridState(),
                                selectedDirectory = event.directory,
                                previousDirectory = if (event.savePreviousDirectory) it.selectedDirectory
                                else event.directory.parentFile,
                                inNestedDirectory = event.directory != Environment.getExternalStorageDirectory()
                            )
                        }
                    }
                }
            }

            is BrowseEvent.OnGoBackDirectory -> {
                onEvent(
                    BrowseEvent.OnChangeDirectory(
                        _state.value.previousDirectory
                            ?: Environment.getExternalStorageDirectory(),
                        savePreviousDirectory = false
                    )
                )
            }

            is BrowseEvent.OnShowHideFilterBottomSheet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            showFilterBottomSheet = !it.showFilterBottomSheet
                        )
                    }
                }
            }

            is BrowseEvent.OnScrollToFilterPage -> {
                viewModelScope.launch {
                    _state.update {
                        event.pagerState?.scrollToPage(event.page)

                        it.copy(
                            currentPage = event.page
                        )
                    }
                }
            }

            is BrowseEvent.OnUpdateFavoriteDirectory -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    updateFavoriteDirectory.execute(event.path)
                    getFilesFromDownloads()
                }
            }
        }
    }

    fun filterList(mainState: MainState): List<SelectableFile> {
        fun <T> thenCompareBy(
            selector: (T) -> Comparable<*>?
        ): Comparator<T> {
            return if (mainState.browseSortOrderDescending!!) {
                compareByDescending(selector)
            } else {
                compareBy(selector)
            }
        }

        fun List<SelectableFile>.filterFiles(): List<SelectableFile> {
            if (mainState.browseIncludedFilterItems!!.isEmpty()) {
                return this
            }

            return filter { file ->
                when (file.isDirectory) {
                    true -> {
                        return@filter this.filter {
                            if (file == it) {
                                return@filter false
                            }

                            it.fileOrDirectory.path.startsWith(file.fileOrDirectory.path)
                        }.filterFiles().isNotEmpty()
                    }

                    false -> {
                        return@filter mainState.browseIncludedFilterItems.any {
                            file.fileOrDirectory.path.endsWith(
                                it, ignoreCase = true
                            )
                        }
                    }
                }
            }
        }

        return _state.value.selectableFiles
            .filterFiles()
            .filter {
                if (
                    _state.value.hasSearched
                    || mainState.browseFilesStructure == BrowseFilesStructure.ALL_FILES
                ) {
                    return@filter !it.isDirectory
                }

                if (
                    Environment.getExternalStorageDirectory() == _state.value.selectedDirectory
                    && it.isFavorite
                    && mainState.browsePinFavoriteDirectories!!
                ) {
                    return@filter true
                }

                it.parentDirectory == _state.value.selectedDirectory
            }
            .sortedWith(
                compareByDescending<SelectableFile> {
                    when (mainState.browsePinFavoriteDirectories!!) {
                        true -> it.isFavorite
                        false -> true
                    }
                }.then(
                    compareByDescending {
                        when (mainState.browseSortOrder!! != BrowseSortOrder.FILE_TYPE) {
                            true -> it.isDirectory
                            false -> true
                        }
                    }
                ).then(
                    thenCompareBy {
                        when (mainState.browseSortOrder!!) {
                            BrowseSortOrder.NAME -> {
                                it.fileOrDirectory.name.lowercase().trim()
                            }

                            BrowseSortOrder.FILE_TYPE -> {
                                it.isDirectory
                            }

                            BrowseSortOrder.FILE_FORMAT -> {
                                it.fileOrDirectory.extension
                            }

                            BrowseSortOrder.FILE_SIZE -> {
                                it.fileOrDirectory.length()
                            }

                            BrowseSortOrder.LAST_MODIFIED -> {
                                it.fileOrDirectory.lastModified()
                            }
                        }
                    }
                )
            )
    }

    private suspend fun getFilesFromDownloads(
        query: String = if (_state.value.showSearch) _state.value.searchQuery else ""
    ) {
        getFilesFromDevice.execute(query).apply {
            yield()
            _state.update {
                it.copy(
                    selectableFiles = this,
                    selectedItemsCount = 0,
                    hasSearched = query.isNotBlank(),
                    hasSelectedItems = false,
                    isLoading = false
                )
            }
        }
    }

    fun clearViewModel() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(isError = false)
            }
        }
    }
}