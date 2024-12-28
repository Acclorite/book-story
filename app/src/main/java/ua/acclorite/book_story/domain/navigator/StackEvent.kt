package ua.acclorite.book_story.domain.navigator

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
enum class StackEvent : Parcelable {
    Default, Pop
}