package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class LanguageHistory(
    val id: Int,
    val languageCode: String
) : Parcelable
