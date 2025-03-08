/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.core.constants

import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.about.Badge

fun Constants.provideAboutBadges() = listOf(
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
    ),
)