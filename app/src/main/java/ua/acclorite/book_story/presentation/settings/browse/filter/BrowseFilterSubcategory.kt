@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse.filter

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.browse.filter.components.BrowseFilterOption
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory

fun LazyListScope.BrowseFilterSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.filter_browse_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider
    ) {
        BrowseFilterOption()
    }
}