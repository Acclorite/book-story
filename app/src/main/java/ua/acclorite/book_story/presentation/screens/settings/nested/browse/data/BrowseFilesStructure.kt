package ua.acclorite.book_story.presentation.screens.settings.nested.browse.data

import androidx.compose.runtime.Immutable

@Immutable
enum class BrowseFilesStructure {
    ALL_FILES, DIRECTORIES
}

fun String.toBrowseFilesStructure(): BrowseFilesStructure {
    return BrowseFilesStructure.valueOf(this)
}