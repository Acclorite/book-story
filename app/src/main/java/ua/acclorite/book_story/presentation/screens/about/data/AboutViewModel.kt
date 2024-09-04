package ua.acclorite.book_story.presentation.screens.about.data

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.use_case.CheckForUpdates
import ua.acclorite.book_story.presentation.core.util.BaseViewModel
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val checkForUpdates: CheckForUpdates,
) : BaseViewModel<AboutState, AboutEvent>() {

    private val _state = MutableStateFlow(AboutState())
    override val state = _state.asStateFlow()

    override fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.OnNavigateToBrowserPage -> {
                viewModelScope.launch {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(event.page)
                    )

                    intent.launchActivity(event.context as ComponentActivity) {
                        event.noAppsFound()
                    }
                }
            }

            is AboutEvent.OnCheckForUpdates -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            updateLoading = true
                        )
                    }
                    val result = checkForUpdates.execute(false)

                    if (result == null) {
                        _state.update {
                            it.copy(
                                updateLoading = false
                            )
                        }
                        event.error()
                        return@launch
                    }

                    val version = result.tagName.substringAfterLast("v")
                    val currentVersion = event.context.getString(R.string.app_version)

                    if (version == currentVersion) {
                        event.noUpdatesFound()
                        _state.update {
                            it.copy(
                                updateLoading = false,
                                alreadyCheckedForUpdates = true
                            )
                        }
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            showUpdateDialog = true,
                            updateLoading = false,
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








