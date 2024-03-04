package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Immutable
data class StringWithId(
    val line: String,
    val id: String = UUID.randomUUID().toString()
) : Parcelable
