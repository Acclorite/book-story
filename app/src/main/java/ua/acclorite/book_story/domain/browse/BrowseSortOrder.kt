package ua.acclorite.book_story.domain.browse

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