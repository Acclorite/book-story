package ua.acclorite.book_story.presentation.screens.settings.nested.browse.data

enum class BrowseSortOrder {
    NAME,
    FILE_FORMAT,
    FILE_TYPE,
    LAST_MODIFIED,
    FILE_SIZE,
}

fun String.toBrowseSortOrder(): BrowseSortOrder {
    return BrowseSortOrder.valueOf(this)
}