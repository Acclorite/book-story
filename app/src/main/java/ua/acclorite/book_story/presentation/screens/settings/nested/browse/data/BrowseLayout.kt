package ua.acclorite.book_story.presentation.screens.settings.nested.browse.data

enum class BrowseLayout {
    LIST, GRID
}

fun String.toBrowseLayout(): BrowseLayout {
    return BrowseLayout.valueOf(this)
}