package ua.acclorite.book_story.presentation.screens.about.nested.license_info.data

import androidx.compose.runtime.Immutable
import com.mikepenz.aboutlibraries.entity.Library

@Immutable
data class LicenseInfoState(
    val license: Library? = null,
)