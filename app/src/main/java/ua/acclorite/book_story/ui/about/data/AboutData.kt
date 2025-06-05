/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about.data

import kotlinx.collections.immutable.persistentListOf
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.about.model.Badge

object AboutData {
    val badges = persistentListOf(
        Badge(
            id = "github",
            drawable = R.drawable.github,
            imageVector = null,
            contentDescription = R.string.github_badge,
            url = "https://www.github.com/Acclorite"
        ),
        Badge(
            id = "reddit",
            drawable = R.drawable.reddit,
            imageVector = null,
            contentDescription = R.string.reddit_badge,
            url = "https://www.reddit.com/user/Acclorite/"
        ),
        Badge(
            id = "tryzub",
            drawable = R.drawable.tryzub,
            imageVector = null,
            contentDescription = R.string.tryzub_badge,
            url = null
        ),
        Badge(
            id = "gitlab",
            drawable = R.drawable.gitlab,
            imageVector = null,
            contentDescription = R.string.gitlab_badge,
            url = "https://www.gitlab.com/Acclorite"
        ),
        Badge(
            id = "codeberg",
            drawable = R.drawable.codeberg,
            imageVector = null,
            contentDescription = R.string.codeberg_badge,
            url = "https://www.codeberg.org/Acclorite"
        )
    )
}