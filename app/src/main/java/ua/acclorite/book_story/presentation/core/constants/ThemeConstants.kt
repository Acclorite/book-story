package ua.acclorite.book_story.presentation.core.constants

import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.ui.Theme

fun Constants.provideThemes() = listOf(
    Pair(Theme.DYNAMIC, UIText.StringResource(R.string.dynamic_theme)),
    Pair(Theme.BLUE, UIText.StringResource(R.string.blue_theme)),
    Pair(Theme.GREEN, UIText.StringResource(R.string.green_theme)),
    Pair(Theme.MARSH, UIText.StringResource(R.string.marsh_theme)),
    Pair(Theme.RED, UIText.StringResource(R.string.red_theme)),
    Pair(Theme.PURPLE, UIText.StringResource(R.string.purple_theme)),
    Pair(Theme.LAVENDER, UIText.StringResource(R.string.lavender_theme)),
    Pair(Theme.PINK, UIText.StringResource(R.string.pink_theme)),
    Pair(Theme.YELLOW, UIText.StringResource(R.string.yellow_theme)),
    Pair(Theme.AQUA, UIText.StringResource(R.string.aqua_theme)),
)