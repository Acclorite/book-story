package ua.acclorite.book_story.domain.navigator

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
enum class StackEvent {
    Default, Pop
}