package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Keep
data class LanguageHistory(
    val id: Int,
    val languageCode: String
) : Parcelable
