/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.helpers

inline fun Boolean.ifTrue(perform: () -> Unit) {
    if (this) perform()
}

inline fun Boolean.ifFalse(perform: () -> Unit) {
    if (!this) perform()
}