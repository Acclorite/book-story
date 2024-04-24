package ua.acclorite.book_story.presentation.screens.about.data

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.use_case.CheckForUpdates
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val checkForUpdates: CheckForUpdates,
) : ViewModel() {

    private val _state = MutableStateFlow(AboutState())
    val state = _state.asStateFlow()

    fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.OnNavigateToBrowserPage -> {
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

            is AboutEvent.OnCheckForUpdates -> {
                viewModelScope.launch {
                    val result = checkForUpdates.execute(false)

                    if (result == null) {
                        event.error()
                        return@launch
                    }

                    val version = result.tagName.substringAfterLast("v")
                    val currentVersion = event.context.getString(R.string.app_version)

                    if (version == currentVersion) {
                        event.noUpdatesFound()
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            showUpdateDialog = true,
                            updateInfo = result
                        )
                    }
                }
            }

            is AboutEvent.OnDismissUpdateDialog -> {
                _state.update {
                    it.copy(
                        showUpdateDialog = false
                    )
                }
            }
        }
    }
}








