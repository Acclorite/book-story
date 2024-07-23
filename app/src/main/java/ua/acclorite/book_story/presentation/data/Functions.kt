package ua.acclorite.book_story.presentation.data

import android.content.Intent
import androidx.activity.ComponentActivity

fun String.removeTrailingZero(): String {
    if (!this.contains('.'))
        return this
    return this
        .dropLastWhile { it == '0' }
        .dropLastWhile { it == '.' }
}

fun Double.removeDigits(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

fun Intent.launchActivity(
    activity: ComponentActivity,
    openInNewWindow: Boolean = true,
    success: (() -> Unit)? = null,
    error: () -> Unit
) {
    if (openInNewWindow) {
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        activity.baseContext.startActivity(this)
    } catch (e: Exception) {
        error()
        return
    }

    success?.invoke()
}