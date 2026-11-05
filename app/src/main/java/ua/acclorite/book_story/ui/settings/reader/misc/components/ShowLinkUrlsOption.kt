/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.misc.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Composable
fun ShowLinkUrlsOption() {
    val settings = LocalSettings.current

    SwitchWithTitle(
        selected = settings.showLinkUrls.value,
        title = stringResource(id = R.string.show_link_urls_option),
        onClick = {
            settings.showLinkUrls.update(!settings.showLinkUrls.lastValue)
        }
    )
}
