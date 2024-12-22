package ua.acclorite.book_story.ui.browse

import android.os.Environment
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.SelectableNullableBook
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import java.io.File

@Immutable
data class BrowseState(
    val files: List<SelectableFile> = emptyList(),

    val selectedDirectory: File = Environment.getExternalStorageDirectory(),
    val previousDirectory: File? = null,
    val inNestedDirectory: Boolean = false,

    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasSearched: Boolean = false,
    val hasFocused: Boolean = false,

    val dialog: Dialog? = null,
    val bottomSheet: BottomSheet? = null,

    val selectedBooksAddDialog: List<SelectableNullableBook> = emptyList(),
    val loadingAddDialog: Boolean = false
)