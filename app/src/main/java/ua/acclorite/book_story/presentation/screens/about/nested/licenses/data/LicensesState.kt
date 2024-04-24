package ua.acclorite.book_story.presentation.screens.about.nested.licenses.data

import androidx.compose.runtime.Immutable
import com.mikepenz.aboutlibraries.entity.Library

@Immutable
data class LicensesState(
    val licenses: List<Library> = emptyList()
)
