package ua.acclorite.book_story.presentation.screens.help.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.presentation.data.Screen
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(HelpState())
    val state = _state.asStateFlow()

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