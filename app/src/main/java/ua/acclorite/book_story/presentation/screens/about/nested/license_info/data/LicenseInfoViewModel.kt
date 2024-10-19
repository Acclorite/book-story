package ua.acclorite.book_story.presentation.screens.about.nested.license_info.data

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.util.UIViewModel
import ua.acclorite.book_story.presentation.core.util.launchActivity
import javax.inject.Inject

@HiltViewModel
class LicenseInfoViewModel @Inject constructor(

) : UIViewModel<LicenseInfoState, LicenseInfoEvent>() {

    companion object {
        @Composable
        fun getState() = getState<LicenseInfoViewModel, LicenseInfoState, LicenseInfoEvent>()

        @Composable
        fun getEvent() = getEvent<LicenseInfoViewModel, LicenseInfoState, LicenseInfoEvent>()
    }

    private val _state = MutableStateFlow(LicenseInfoState())
    override val state = _state.asStateFlow()

    override fun onEvent(event: LicenseInfoEvent) {
        when (event) {
            is LicenseInfoEvent.OnInit -> init(event)

            is LicenseInfoEvent.OnOpenLicensePage -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(event.page)
                )

                intent.launchActivity(event.context as ComponentActivity) {
                    event.noAppsFound()
                }
            }
        }
    }

    private fun init(event: LicenseInfoEvent.OnInit) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(license = null)
            }

            val license = Libs.Builder().withContext(event.context).build().libraries.find {
                it.uniqueId == event.screen.licenseId
            }

            if (license == null) {
                event.navigateBack()
                return@launch
            }

            _state.update {
                it.copy(
                    license = license
                )
            }
        }
    }
}