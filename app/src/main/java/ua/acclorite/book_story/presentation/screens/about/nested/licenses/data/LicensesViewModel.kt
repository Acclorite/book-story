package ua.acclorite.book_story.presentation.screens.about.nested.licenses.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(LicensesState())
    val state = _state.asStateFlow()

    fun init(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.licenses.isNotEmpty()) {
                return@launch
            }

            val licenses = Libs
                .Builder()
                .withJson(context, R.raw.aboutlibraries)
                .build()
                .libraries
                .toList()

            _state.update {
                it.copy(
                    licenses = licenses.sortedBy { library -> library.openSource }
                )
            }
        }
    }
}