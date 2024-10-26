package ua.acclorite.book_story.presentation.core.util

import android.content.Intent
import android.view.WindowManager
import androidx.activity.ComponentActivity

fun Intent.launchActivity(
    activity: ComponentActivity,
    createChooser: Boolean = false,
    openInNewWindow: Boolean = true,
    error: () -> Unit
) {
    val intent = if (createChooser) Intent.createChooser(this, "") else this
    if (openInNewWindow) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        activity.baseContext.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        error()
        return
    }
}

fun ComponentActivity.setBrightness(brightness: Float?) {
    window.attributes.apply {
        screenBrightness = brightness ?: WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        window.attributes = this
    }
}