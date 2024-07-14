package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class ReaderLine(
    val line: String
) : Parcelable