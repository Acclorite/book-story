package ua.acclorite.book_story.data.remote.dto

import androidx.compose.runtime.Immutable

@Suppress("PropertyName")
@Immutable
data class ReleaseResponse(
    val name: String,
    val tag_name: String
)
