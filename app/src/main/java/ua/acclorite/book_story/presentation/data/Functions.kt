package ua.acclorite.book_story.presentation.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity

var toast: Toast? = null

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

fun String.showToast(context: Context, longToast: Boolean = true) {
    toast?.cancel()
    toast = null

    toast = Toast.makeText(context, this, if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}