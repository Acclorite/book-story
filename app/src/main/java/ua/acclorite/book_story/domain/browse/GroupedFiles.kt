package ua.acclorite.book_story.domain.browse

import androidx.compose.runtime.Immutable

@Immutable
data class GroupedFiles(
    val header: String,
    val pinned: Boolean,
    val files: List<SelectableFile>
)