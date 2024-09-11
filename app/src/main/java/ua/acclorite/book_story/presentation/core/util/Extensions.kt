package ua.acclorite.book_story.presentation.core.util

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier

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
    createChooser: Boolean = false,
    openInNewWindow: Boolean = true,
    success: (() -> Unit)? = null,
    error: () -> Unit
) {
    val intent = if (createChooser) Intent.createChooser(this, "") else this
    if (openInNewWindow) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        activity.baseContext.startActivity(intent)
    } catch (e: Exception) {
        error()
        return
    }

    success?.invoke()
}

fun Float.calculateProgress(digits: Int): String {
    return (this * 100)
        .toDouble()
        .removeDigits(digits)
        .removeTrailingZero()
        .dropWhile { it == '-' }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
): Modifier {
    return this.combinedClickable(
        indication = null,
        interactionSource = null,
        enabled = enabled,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
        onClick = onClick
    )
}