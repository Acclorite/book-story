package ua.acclorite.book_story.domain.ui

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Immutable
sealed class UIText : Parcelable {
    data class StringValue(val value: String) : UIText()
    class StringResource(@StringRes val resId: Int, vararg val args: Serializable) : UIText()

    @Composable
    fun asString(): String {
        return when (this) {
            is StringValue -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringValue -> value
            is StringResource -> context.getString(resId, *args)
        }
    }

    fun getAsString(): String? {
        return when (this) {
            is StringValue -> value
            is StringResource -> null
        }
    }
}