/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.SettingsEffect
import ua.acclorite.book_story.ui.common.helpers.showToast

@Composable
fun SettingsEffects(effects: SharedFlow<SettingsEffect>) {
    val context = LocalContext.current

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is SettingsEffect.OnScroll -> {
                    effect.listState.requestScrollToItem(effect.index, effect.offset)
                }

                is SettingsEffect.OnSwitchedColorPreset -> {
                    context.getString(
                        R.string.color_preset_selected_query,
                        if (effect.newColorPreset.name.isBlank()) {
                            context.getString(
                                R.string.color_preset_query,
                                effect.newColorPreset.id.toString()
                            )
                        } else {
                            effect.newColorPreset.name
                        }.trim()
                    ).showToast(context, longToast = false)
                }
            }
        }
    }
}