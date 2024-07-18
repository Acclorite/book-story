@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.subcategories.FontSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.subcategories.TextSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.subcategories.TranslatorSubcategory

/**
 * Reader Settings Category.
 * Contains all Reader settings.
 *
 * @param state [MainState] instance.
 * @param onMainEvent [MainEvent] callback.
 * @param titleColor Color that [CategoryTitle] has.
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.ReaderSettingsCategory(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    FontSubcategory(
        state = state,
        onMainEvent = onMainEvent,
        titleColor = titleColor,
        topPadding = topPadding,
        bottomPadding = 0.dp
    )
    TextSubcategory(
        state,
        onMainEvent = onMainEvent,
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    TranslatorSubcategory(
        state,
        onMainEvent = onMainEvent,
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = bottomPadding
    )
}