package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.util.UIText

// Main State
fun Constants.provideMainState() = "main_state"

// Empty Book
fun Constants.provideEmptyBook() = Book(
    id = -1,
    title = "",
    author = UIText.StringValue(""),
    description = null,
    textPath = "",
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

// Primary Scrollbar
@Composable
fun Constants.providePrimaryScrollbar() = ScrollbarSettings(
    thumbUnselectedColor = MaterialTheme.colorScheme.primary,
    thumbSelectedColor = MaterialTheme.colorScheme.primary.copy(0.8f),
    hideDelayMillis = 2000,
    durationAnimationMillis = 300,
    selectionMode = ScrollbarSelectionMode.Thumb,
    thumbThickness = 8.dp,
    scrollbarPadding = 4.dp
)

// Secondary Scrollbar
@Composable
fun Constants.provideSecondaryScrollbar() = ScrollbarSettings(
    thumbUnselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
    hideDelayMillis = 500,
    durationAnimationMillis = 200,
    thumbMinLength = 0.4f,
    selectionMode = ScrollbarSelectionMode.Disabled,
    thumbThickness = 4.dp,
    thumbShape = RectangleShape,
    hideDisplacement = 0.dp,
    scrollbarPadding = 0.dp
)

// Links
fun Constants.provideDownloadLatestReleasePage() =
    "https://www.github.com/Acclorite/book-story/releases/latest/download/book-story.apk"

fun Constants.provideReleasesPage() =
    "https://www.github.com/Acclorite/book-story/releases/latest"

fun Constants.provideIssuesPage() =
    "https://www.github.com/Acclorite/book-story/issues"

fun Constants.provideContributorsPage() =
    "https://github.com/Acclorite/book-story/graphs/contributors"

fun Constants.provideTranslationPage() =
    "https://hosted.weblate.org/projects/book-story"