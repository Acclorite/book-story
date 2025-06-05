/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.navigator

import androidx.compose.runtime.compositionLocalOf
import ua.acclorite.book_story.presentation.navigator.Navigator

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator was not passed.") }