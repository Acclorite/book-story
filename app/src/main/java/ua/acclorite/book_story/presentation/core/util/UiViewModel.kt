package ua.acclorite.book_story.presentation.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow


abstract class UiViewModel<S, E> : ViewModel() {
    abstract val state: StateFlow<S>
    abstract fun onEvent(event: E)

    companion object {
        @Composable
        inline fun <reified V : UiViewModel<S, E>, S, E> getState(): State<S> {
            return hiltViewModel<V>().state.collectAsStateWithLifecycle()
        }

        @Composable
        inline fun <reified V : UiViewModel<S, E>, S, E> getEvent(): (E) -> Unit {
            return hiltViewModel<V>()::onEvent
        }
    }
}