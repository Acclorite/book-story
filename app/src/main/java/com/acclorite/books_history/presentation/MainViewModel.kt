package com.acclorite.books_history.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.domain.use_case.ChangeLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Stores all variables such as theme, language etc
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: BookRepository,

    private val changeLanguage: ChangeLanguage
) : ViewModel() {


}