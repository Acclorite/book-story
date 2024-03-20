package ua.acclorite.book_story.presentation.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.ui.DarkTheme
import ua.acclorite.book_story.presentation.ui.Theme

/**
 * Main State. All app's settings are here. Wrapped in SavedStateHandle, so it won't reset.
 */
@Immutable
data class MainState(
    val language: String? = null,
    val theme: Theme? = null,
    val darkTheme: DarkTheme? = null,
    val fontFamily: String? = null,
    val isItalic: Boolean? = null,
    val fontSize: Int? = null,
    val lineHeight: Int? = null,
    val paragraphHeight: Int? = null,
    val paragraphIndentation: Boolean? = null,
    val backgroundColor: Long? = null,
    val fontColor: Long? = null,
    val showStartScreen: Boolean? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        // String
        language = parcel.readString(),
        // String
        theme = Theme.valueOf(parcel.readString() ?: Theme.BLUE.name),
        // String
        darkTheme = DarkTheme.valueOf(parcel.readString() ?: DarkTheme.FOLLOW_SYSTEM.name),
        // String
        fontFamily = parcel.readString(),
        // Boolean
        isItalic = if (parcel.readByte().toInt() != 0) parcel.readByte().toInt() == 1 else null,
        // Int
        fontSize = if (parcel.readByte().toInt() != 0) parcel.readInt() else null,
        // Int
        lineHeight = if (parcel.readByte().toInt() != 0) parcel.readInt() else null,
        // Int
        paragraphHeight = if (parcel.readByte().toInt() != 0) parcel.readInt() else null,
        // Boolean
        paragraphIndentation = if (parcel.readByte().toInt() != 0) parcel.readByte()
            .toInt() == 1 else null,
        // Long
        backgroundColor = if (parcel.readByte().toInt() != 0) parcel.readLong() else null,
        // Long
        fontColor = if (parcel.readByte().toInt() != 0) parcel.readLong() else null,
        // Boolean
        showStartScreen = if (parcel.readByte().toInt() != 0) parcel.readByte()
            .toInt() == 1 else null,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Language
        parcel.writeString(language)
        // Theme
        parcel.writeString(theme?.name)
        // Dark Theme
        parcel.writeString(darkTheme?.name)
        // Font Family
        parcel.writeString(fontFamily)
        // Is Italic
        parcel.writeByte((if (isItalic != null) 1 else 0).toByte())
        isItalic?.let { parcel.writeByte(if (it) 1 else 0) }
        // Font Size
        parcel.writeByte((if (fontSize != null) 1 else 0).toByte())
        fontSize?.let { parcel.writeInt(it) }
        // Line Height
        parcel.writeByte((if (lineHeight != null) 1 else 0).toByte())
        lineHeight?.let { parcel.writeInt(it) }
        // Paragraph Height
        parcel.writeByte((if (paragraphHeight != null) 1 else 0).toByte())
        paragraphHeight?.let { parcel.writeInt(it) }
        // Paragraph Indentation
        parcel.writeByte((if (paragraphIndentation != null) 1 else 0).toByte())
        paragraphIndentation?.let { parcel.writeByte(if (it) 1 else 0) }
        // Background color
        parcel.writeByte((if (backgroundColor != null) 1 else 0).toByte())
        backgroundColor?.let { parcel.writeLong(it) }
        // Font color
        parcel.writeByte((if (fontColor != null) 1 else 0).toByte())
        fontColor?.let { parcel.writeLong(it) }
        // Show Start Screen
        parcel.writeByte((if (showStartScreen != null) 1 else 0).toByte())
        showStartScreen?.let { parcel.writeByte(if (it) 1 else 0) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainState> {
        override fun createFromParcel(parcel: Parcel): MainState {
            return MainState(parcel)
        }

        override fun newArray(size: Int): Array<MainState?> {
            return arrayOfNulls(size)
        }
    }
}