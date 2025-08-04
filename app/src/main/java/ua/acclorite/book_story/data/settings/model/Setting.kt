/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.settings.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Stable
class Setting<T, P>(
    val key: Preferences.Key<P>,
    private val default: T,
    private val setSetting: (P) -> Unit,
    private val serialize: (T) -> P,
    private val deserialize: (P) -> T
) {
    private val _value = MutableStateFlow<T>(default)
    val value: T
        @Composable get() = _value.collectAsStateWithLifecycle().value
    val lastValue: T
        get() = _value.value

    fun update(value: T) {
        _value.update { value }
        setSetting(serialize(value))
    }

    fun init(value: P?) {
        if (value == null) {
            setSetting(serialize(default))
            return
        }
        _value.update { deserialize(value) }
    }
}