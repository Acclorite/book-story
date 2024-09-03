package ua.acclorite.book_story.domain.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S, E> : ViewModel() {
    abstract val state: StateFlow<S>
    abstract fun onEvent(event: E)
}