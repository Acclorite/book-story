@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.general.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings.AppLanguageSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings.CheckForUpdatesSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings.DoublePressExitSetting

/**
 * General Settings Category.
 * Contains all General settings such as App Language.
 *
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.GeneralSettingsCategory(
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    item {
        Spacer(modifier = Modifier.height((topPadding - 8.dp).coerceAtLeast(0.dp)))
    }

    item {
        AppLanguageSetting()
    }

    item {
        CheckForUpdatesSetting()
    }

    item {
        DoublePressExitSetting()
    }

    item {
        Spacer(modifier = Modifier.height(bottomPadding))
    }
}