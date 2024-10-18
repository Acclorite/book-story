package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.HelpTip
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation

fun Constants.provideHelpTips() = listOf(
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

    HelpTip(
        title = R.string.help_title_how_to_use_perception_expander,
        description = { _, _ ->
            append(stringResource(id = R.string.help_title_how_to_use_perception_expander_1))
        }
    ),
)