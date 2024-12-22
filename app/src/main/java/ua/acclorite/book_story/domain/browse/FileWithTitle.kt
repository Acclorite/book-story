package ua.acclorite.book_story.domain.browse

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.ui.UIText
import java.io.File

@Immutable
data class FileWithTitle(
    val title: UIText,
    val file: File
)