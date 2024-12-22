package ua.acclorite.book_story.domain.reader

import android.content.pm.ActivityInfo

enum class ReaderScreenOrientation(val code: Int) {
    DEFAULT(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED),
    FREE(ActivityInfo.SCREEN_ORIENTATION_USER),
    PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT),
    LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE),
    LOCKED_PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    LOCKED_LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
}

fun String.toReaderScreenOrientation(): ReaderScreenOrientation {
    return ReaderScreenOrientation.valueOf(this)
}