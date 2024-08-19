package ua.acclorite.book_story.domain.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Badge
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.model.Credit
import ua.acclorite.book_story.domain.model.FontWithName
import ua.acclorite.book_story.domain.model.HelpTip
import ua.acclorite.book_story.domain.model.NavigationItem
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.content.HelpFindBooksContent
import ua.acclorite.book_story.presentation.ui.Theme

object Constants {

    // Main State Constant
    const val MAIN_STATE = "main_state"

    // Supported file extensions
    val EXTENSIONS = listOf(".epub", ".pdf", ".fb2", ".txt")

    // Supported languages
    val LANGUAGES = listOf(
        Pair("en", "English"),
        Pair("uk", "Українська"),
    )

    // Navigation items for NavigationBars.
    val NAVIGATION_ITEMS = listOf(
        NavigationItem(
            screen = Screen.Library,
            title = R.string.library_screen,
            tooltip = R.string.library_content_desc,
            selectedIcon = R.drawable.library_screen_filled,
            unselectedIcon = R.drawable.library_screen_outlined
        ),
        NavigationItem(
            screen = Screen.History,
            title = R.string.history_screen,
            tooltip = R.string.history_content_desc,
            selectedIcon = R.drawable.history_screen_filled,
            unselectedIcon = R.drawable.history_screen_outlined
        ),
        NavigationItem(
            screen = Screen.Browse,
            title = R.string.browse_screen,
            tooltip = R.string.browse_content_desc,
            selectedIcon = R.drawable.browse_screen_filled,
            unselectedIcon = R.drawable.browse_screen_outlined
        )
    )

    // Supported themes
    val THEMES = listOf(
        Pair(Theme.DYNAMIC, UIText.StringResource(R.string.dynamic_theme)),
        Pair(Theme.BLUE, UIText.StringResource(R.string.blue_theme)),
        Pair(Theme.GREEN, UIText.StringResource(R.string.green_theme)),
        Pair(Theme.RED, UIText.StringResource(R.string.red_theme)),
        Pair(Theme.PURPLE, UIText.StringResource(R.string.purple_theme)),
        Pair(Theme.PINK, UIText.StringResource(R.string.pink_theme)),
        Pair(Theme.YELLOW, UIText.StringResource(R.string.yellow_theme)),
        Pair(Theme.AQUA, UIText.StringResource(R.string.aqua_theme)),
    )

    // Credits
    val CREDITS = listOf(
        Credit(
            name = "Tachiyomi (Mihon)",
            source = "GitHub",
            credits = listOf(
                UIText.StringResource(R.string.credits_design),
                UIText.StringValue("Readme")
            ),
            website = "https://www.github.com/mihonapp/mihon"
        ),
        Credit(
            name = "Kitsune",
            source = "GitHub",
            credits = listOf(
                UIText.StringResource(R.string.credits_updates)
            ),
            website = "https://www.github.com/Drumber/Kitsune"
        ),
        Credit(
            name = "Material Design Icons",
            source = "Google Fonts",
            credits = listOf(
                UIText.StringResource(R.string.credits_icon)
            ),
            website = "https://fonts.google.com/icons"
        ),
    )

    // Help Tips
    val HELP_TIPS = listOf(
        HelpTip(
            title = R.string.help_title_how_to_find_books,
            description = { _, _ ->
                append(stringResource(id = R.string.help_desc_how_to_find_books))
            },
            customContent = { onHelpEvent ->
                HelpFindBooksContent(onEvent = onHelpEvent)
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_add_books,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_add_books_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Browse, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_add_books_2))
                }
                append(". ")

                append(stringResource(id = R.string.help_desc_how_to_add_books_3) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Library, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_add_books_4))
                }
                append(".")
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_customize_app,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_customize_app_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Settings, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_customize_app_2))
                }
                append(". ")

                append(stringResource(id = R.string.help_desc_how_to_customize_app_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_move_or_delete_books,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_move_or_delete_books_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Library, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_move_or_delete_books_2))
                }
                append(". ")

                append(stringResource(id = R.string.help_desc_how_to_move_or_delete_books_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_edit_book,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_edit_book_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Library, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_edit_book_2))
                }
                append(" ")

                append(stringResource(id = R.string.help_desc_how_to_edit_book_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_read_book,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_read_book_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Library, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_read_book_2))
                }
                append(". ")

                append(stringResource(id = R.string.help_desc_how_to_read_book_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_customize_reader,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_customize_reader_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Settings, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_customize_reader_2))
                }
                append(" ")

                append(stringResource(id = R.string.help_desc_how_to_customize_reader_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_update_book,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_update_book_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.Library, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_update_book_2))
                }
                append(" ")

                append(stringResource(id = R.string.help_desc_how_to_update_book_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_manage_history,
            description = { onNavigate, fromStart ->
                append(stringResource(id = R.string.help_desc_how_to_manage_history_1) + " ")

                HelpAnnotation(
                    onClick = {
                        if (!fromStart) {
                            onNavigate {
                                navigate(Screen.History, useBackAnimation = true)
                            }
                        }
                    }
                ) {
                    append(stringResource(id = R.string.help_desc_how_to_manage_history_2))
                }
                append(". ")

                append(stringResource(id = R.string.help_desc_how_to_manage_history_3))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_use_tooltip,
            description = { _, _ ->
                append(stringResource(id = R.string.help_desc_how_to_use_tooltip_1))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_use_double_click_translation,
            description = { _, _ ->
                append(stringResource(id = R.string.help_desc_how_to_use_double_click_translation_1))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_create_color_presets,
            description = { _, _ ->
                append(stringResource(id = R.string.help_desc_how_to_create_color_presets_1))
            }
        ),

        HelpTip(
            title = R.string.help_title_how_to_use_color_presets,
            description = { _, _ ->
                append(stringResource(id = R.string.help_desc_how_to_use_color_presets_1))
            }
        ),
    )

    // About Badges
    val ABOUT_BADGES = listOf(
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
            id = "github_project",
            drawable = R.drawable.github,
            imageVector = null,
            contentDescription = R.string.github_project_content_desc,
            url = "https://www.github.com/Acclorite/book-story"
        ),
        Badge(
            id = "github_profile",
            drawable = null,
            imageVector = Icons.Default.Person,
            contentDescription = R.string.github_profile_content_desc,
            url = "https://www.github.com/Acclorite"
        ),
    )

    // Fonts for Reader
    val FONTS = listOf(
        FontWithName(
            "default",
            UIText.StringResource(R.string.default_font),
            FontFamily.Default
        ),
        FontWithName(
            "raleway",
            UIText.StringValue("Raleway"),
            FontFamily(
                Font(R.font.raleway_regular),
                Font(R.font.raleway_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "open_sans",
            UIText.StringValue("Open Sans"),
            FontFamily(
                Font(R.font.opensans_regular),
                Font(R.font.opensans_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "mulish",
            UIText.StringValue("Mulish"),
            FontFamily(
                Font(R.font.mulish_regular),
                Font(R.font.mulish_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "arimo",
            UIText.StringValue("Arimo"),
            FontFamily(
                Font(R.font.arimo_regular),
                Font(R.font.arimo_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "garamond",
            UIText.StringValue("Garamond"),
            FontFamily(
                Font(R.font.garamond_regular),
                Font(R.font.garamond_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "roboto_serif",
            UIText.StringValue("Roboto Serif"),
            FontFamily(
                Font(R.font.robotoserif_regular),
                Font(R.font.robotoserif_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "noto_serif",
            UIText.StringValue("Noto Serif"),
            FontFamily(
                Font(R.font.notoserif_regular),
                Font(R.font.notoserif_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "noto_sans",
            UIText.StringValue("Noto Sans"),
            FontFamily(
                Font(R.font.notosans_regular),
                Font(R.font.notosans_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "roboto",
            UIText.StringValue("Roboto"),
            FontFamily(
                Font(R.font.roboto_regular),
                Font(R.font.roboto_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "jost",
            UIText.StringValue("Jost"),
            FontFamily(
                Font(R.font.jost_regular),
                Font(R.font.jost_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "merriweather",
            UIText.StringValue("Merriweather"),
            FontFamily(
                Font(R.font.merriweather_regular),
                Font(R.font.merriweather_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "montserrat",
            UIText.StringValue("Montserrat"),
            FontFamily(
                Font(R.font.montserrat_regular),
                Font(R.font.montserrat_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "nunito",
            UIText.StringValue("Nunito"),
            FontFamily(
                Font(R.font.nunito_regular),
                Font(R.font.nunito_regular_italic, style = FontStyle.Italic)
            )
        ),
        FontWithName(
            "roboto_slab",
            UIText.StringValue("Roboto Slab"),
            FontFamily(
                Font(R.font.robotoslab_regular)
            )
        ),
        FontWithName(
            "lora",
            UIText.StringValue("Lora"),
            FontFamily(
                Font(R.font.lora_regular),
                Font(R.font.lora_regular_italic, style = FontStyle.Italic)
            )
        )
    )

    val EMPTY_BOOK = Book(
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

    val DEFAULT_COLOR_PRESET = ColorPreset(
        id = -1,
        name = null,
        backgroundColor = Color.DarkGray,
        fontColor = Color.LightGray,
        isSelected = false
    )
}