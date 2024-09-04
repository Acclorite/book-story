package ua.acclorite.book_story.presentation.screens.help.data

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(

) : BaseViewModel<HelpState, HelpEvent>() {

    private val _state = MutableStateFlow(HelpState())
    override val state = _state.asStateFlow()

    override fun onEvent(event: HelpEvent) {}

    fun init(screen: Screen.Help) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    fromStart = screen.fromStart
                )
            }
        }
    }
}