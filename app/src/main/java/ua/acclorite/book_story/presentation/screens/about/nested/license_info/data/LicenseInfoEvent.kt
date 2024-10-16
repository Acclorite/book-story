package ua.acclorite.book_story.presentation.screens.about.nested.license_info.data

import android.content.Context
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.core.navigation.Screen

@Immutable
sealed class LicenseInfoEvent {
    data class OnInit(
        val screen: Screen.About.LicenseInfo,
        val navigateBack: () -> Unit,
        val context: Context
    ) : LicenseInfoEvent()

    data class OnOpenLicensePage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : LicenseInfoEvent()
}