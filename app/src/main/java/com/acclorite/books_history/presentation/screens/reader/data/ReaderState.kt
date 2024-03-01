package com.acclorite.books_history.presentation.screens.reader.data

import androidx.compose.runtime.Immutable
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.util.UIText

@Immutable
data class ReaderState(
    val book: Book,
    val errorMessage: UIText? = null,

    val showMenu: Boolean = false,

    val showSettingsBottomSheet: Boolean = false,
    val currentPage: Int = 0,


    )