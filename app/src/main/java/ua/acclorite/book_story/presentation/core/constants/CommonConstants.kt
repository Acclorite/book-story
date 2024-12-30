@file:Suppress("SameReturnValue")

package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.reader.ColorPreset
import ua.acclorite.book_story.domain.ui.UIText

// Main State
fun Constants.provideMainState() = "main_state"

// Empty Book
fun Constants.provideEmptyBook() = Book(
    id = -1,
    title = "",
    author = UIText.StringValue(""),
    description = null,
    filePath = "",
    coverImage = null,
    scrollIndex = 0,
    scrollOffset = 0,
    progress = 0f,
    lastOpened = null,
    category = Category.READING
)

// Default Color Preset
fun Constants.provideDefaultColorPreset() = ColorPreset(
    id = -1,
    name = null,
    backgroundColor = Color(0xFFFAF8FF), // Blue Light Surface (hardcoded)
    fontColor = Color(0xFF44464F), // Blue Light OnSurfaceVariant (hardcoded)
    isSelected = false
)