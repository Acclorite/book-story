package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Badge

fun Constants.provideAboutBadges() = listOf(
    Badge(
        id = "x",
        drawable = R.drawable.x_logo,
        imageVector = null,
        contentDescription = R.string.x_content_desc,
        url = "https://www.x.com/acclorite"
    ),
    Badge(
        id = "reddit",
        drawable = R.drawable.reddit,
        imageVector = null,
        contentDescription = R.string.reddit_content_desc,
        url = "https://www.reddit.com/user/Acclorite/"
    ),
    Badge(
        id = "tryzub",
        drawable = R.drawable.tryzub,
        imageVector = null,
        contentDescription = R.string.tryzub_content_desc,
        url = null
    ),
    Badge(
        id = "patreon",
        drawable = R.drawable.patreon,
        imageVector = null,
        contentDescription = R.string.patreon_content_desc,
        url = "https://www.patreon.com/Acclorite"
    ),
    Badge(
        id = "github_profile",
        drawable = null,
        imageVector = Icons.Default.Person,
        contentDescription = R.string.github_profile_content_desc,
        url = "https://www.github.com/Acclorite"
    ),
)