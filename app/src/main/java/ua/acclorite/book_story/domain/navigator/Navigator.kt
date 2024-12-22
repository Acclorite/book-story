package ua.acclorite.book_story.domain.navigator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.plus

@HiltViewModel(assistedFactory = Navigator.Factory::class)
class Navigator @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Assisted private val initialScreen: Screen
) : ViewModel() {

    val items = savedStateHandle.getStateFlow("items", mutableListOf<Screen>(initialScreen))
    private fun StateFlow<MutableList<Screen>>.removeLast() {
        savedStateHandle["items"] = value.dropLast(1)
    }

    private fun StateFlow<MutableList<Screen>>.add(item: Screen) {
        savedStateHandle["items"] = value + item
    }

    val lastItem = items.map {
        it.lastOrNull() ?: initialScreen
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = initialScreen
    )

    val lastEvent = savedStateHandle.getStateFlow("stack_event", StackEvent.Default)
    private fun StateFlow<StackEvent>.change(stackEvent: StackEvent) {
        savedStateHandle["stack_event"] = stackEvent
    }


    fun push(
        targetScreen: Screen,
        popping: Boolean = false,
        saveInBackStack: Boolean = true
    ) {
        if (lastItem.value::class == targetScreen::class) return
        if (!saveInBackStack) items.removeLast()

        lastEvent.change(
            if (popping) StackEvent.Pop
            else StackEvent.Default
        )

        if (lastItem.value::class == targetScreen::class) items.removeLast()
        items.add(targetScreen)
    }

    fun pop(popping: Boolean = true) {
        if (items.value.count() > 1) {
            lastEvent.change(
                if (popping) StackEvent.Pop
                else StackEvent.Default
            )
            items.removeLast()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(startScreen: Screen): Navigator
    }
}