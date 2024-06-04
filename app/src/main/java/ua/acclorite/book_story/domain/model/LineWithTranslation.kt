package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.util.UIText

@Parcelize
@Immutable
data class LineWithTranslation(
    val originalLine: String,

    val useTranslation: Boolean = false,
    val translatingFrom: String = "",
    val translatingTo: String = "",
    val translatedLine: String? = null,

    val isTranslationLoading: Boolean = false,
    val isTranslationFailed: Boolean = false,
    val errorMessage: UIText? = null
) : Parcelable
