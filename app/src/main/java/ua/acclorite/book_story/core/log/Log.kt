/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.log

import android.util.Log

fun logI(tag: String, message: String) {
    Log.i(tag, message)
}

fun logD(tag: String, message: String) {
    Log.d(tag, message)
}

fun logW(tag: String, message: String) {
    Log.w(tag, message)
}

fun logE(tag: String, message: String) {
    Log.e(tag, message)
}