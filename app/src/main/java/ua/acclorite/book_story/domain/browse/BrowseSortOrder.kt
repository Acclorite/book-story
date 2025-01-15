package ua.acclorite.book_story.domain.browse

enum class BrowseSortOrder {
    NAME,
    FILE_FORMAT,
    LAST_MODIFIED,
    FILE_SIZE,
}

fun String.toBrowseSortOrder(): BrowseSortOrder {
    return try {
        BrowseSortOrder.valueOf(this)
    } catch (_: IllegalArgumentException) {
        BrowseSortOrder.LAST_MODIFIED
    }
}