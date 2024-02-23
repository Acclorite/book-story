package com.acclorite.books_history.presentation.screens.book_info.data

import androidx.compose.runtime.Immutable
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category

@Immutable
data class BookInfoState(
    val book: Book,

    val editTitle: Boolean = false,
    val hasFocused: Boolean = false,
    val titleValue: String = "",

    val showChangeCoverBottomSheet: Boolean = false,
    val showDetailsBottomSheet: Boolean = false,

    val showMoreDropDown: Boolean = false,
    val showDeleteDialog: Boolean = false,

    val showMoveDialog: Boolean = false,
    val selectedCategory: Category = Category.READING,
)
