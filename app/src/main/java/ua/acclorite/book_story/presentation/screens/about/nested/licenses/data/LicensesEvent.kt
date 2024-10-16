package ua.acclorite.book_story.presentation.screens.about.nested.licenses.data

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class LicensesEvent {
    data class OnInit(
        val context: Context
    ) : LicensesEvent()
}