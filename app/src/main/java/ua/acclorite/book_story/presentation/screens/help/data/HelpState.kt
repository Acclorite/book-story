package ua.acclorite.book_story.presentation.screens.help.data

import androidx.compose.runtime.Immutable

@Immutable
data class HelpState(
    val fromStart: Boolean = false,

    val showNote1: Boolean = true,

    val showHelpItem1: Boolean = false,
    val textFieldValue: String = "",
    val showError: Boolean = false,

    val showHelpItem2: Boolean = false,
    val showHelpItem3: Boolean = false,
    val showHelpItem4: Boolean = false,
    val showHelpItem5: Boolean = false,
    val showHelpItem6: Boolean = false,
    val showHelpItem7: Boolean = false,
    val showHelpItem8: Boolean = false,
    val showHelpItem9: Boolean = false,
    val showHelpItem10: Boolean = false,
    val showHelpItem11: Boolean = false,
    val showHelpItem12: Boolean = false,
)