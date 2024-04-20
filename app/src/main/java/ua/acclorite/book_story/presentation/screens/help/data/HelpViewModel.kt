package ua.acclorite.book_story.presentation.screens.help.data

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.presentation.data.Navigator
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(HelpState())
    val state = _state.asStateFlow()

    fun onEvent(event: HelpEvent) {
        when (event) {
            is HelpEvent.OnNavigateToBrowserPage -> {
                viewModelScope.launch {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(event.page)
                    )

                    if (intent.resolveActivity(event.context.packageManager) != null) {
                        event.context.startActivity(intent)
                        return@launch
                    }

                    event.noAppsFound()
                }
            }

            is HelpEvent.OnSearchInWeb -> {
                viewModelScope.launch {
                    if (_state.value.textFieldValue.isBlank()) {
                        _state.update {
                            it.copy(
                                showError = true
                            )
                        }
                        return@launch
                    }

                    val intent = Intent()

                    intent.action = Intent.ACTION_WEB_SEARCH
                    intent.putExtra(
                        SearchManager.QUERY,
                        "${_state.value.textFieldValue.trim()} filetype:txt OR filetype:pdf"
                    )

                    if (intent.resolveActivity(event.context.packageManager) != null) {
                        event.context.startActivity(intent)
                        return@launch
                    }

                    event.noAppsFound()
                }
            }

            is HelpEvent.OnUpdateState -> {
                _state.update {
                    event.block(it)
                }
            }
        }
    }

    fun init(navigator: Navigator) {
        viewModelScope.launch {
            val isFromStart = navigator.retrieveArgument("from_start") as? Boolean ?: false

            _state.update {
                it.copy(
                    fromStart = isFromStart
                )
            }
        }
    }
}








