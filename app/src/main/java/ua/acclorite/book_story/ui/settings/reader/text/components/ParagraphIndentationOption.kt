/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.text.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.presentation.theme.ExpandingTransition
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle

@Composable
fun ParagraphIndentationOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(
        visible = state.value.textAlignment != ReaderTextAlignment.CENTER &&
                state.value.textAlignment != ReaderTextAlignment.END
    ) {
        SliderWithTitle(
            value = state.value.paragraphIndentation to "pt",
            fromValue = 0,
            toValue = 12,
            title = stringResource(id = R.string.paragraph_indentation_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeParagraphIndentation(it)
                )
            }
        )
    }
}