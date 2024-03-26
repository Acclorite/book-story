package ua.acclorite.book_story.presentation.screens.help.data

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(

) : ViewModel() {

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
        }
    }
}








