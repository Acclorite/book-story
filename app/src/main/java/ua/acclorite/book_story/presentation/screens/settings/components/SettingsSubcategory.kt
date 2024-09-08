@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.core.components.CategoryTitle

/**
 * Settings Subcategory.
 * Root for all Subcategories.
 *
 * @param titleColor The color of the title.
 * @param title The title.
 * @param showTitle Whether title should be shown.
 * @param showDivider Whether divider at the end of the category should be shown.
 * @param topPadding Top padding.
 * @param bottomPadding Bottom padding.
 * @param content Settings to be placed inside this subcategory.
 */
fun LazyListScope.SettingsSubcategory(
    titleColor: @Composable () -> Color,
    title: @Composable () -> String,
    showTitle: Boolean,
    showDivider: Boolean,
    topPadding: Dp,
    bottomPadding: Dp,
    content: LazyListScope.() -> Unit
) {
    item {
        if (showTitle) {
            CategoryTitle(
                title = title.invoke(),
                color = titleColor.invoke(),
                modifier = Modifier
                    .padding(
                        top = topPadding,
                        bottom = 8.dp
                    )
            )
        } else {
            Spacer(
                modifier = Modifier.height((topPadding - 8.dp).coerceAtLeast(0.dp))
            )
        }
    }

    content()

    item {
        if (showDivider) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.height(bottomPadding))
    }
}