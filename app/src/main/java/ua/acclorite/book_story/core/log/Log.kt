/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.log

import android.util.Log

fun logI(message: String) {
    Log.i("INFO", message)
}

fun logD(message: String) {
    Log.d("DEBUG", message)
}

fun logW(message: String) {
    Log.w("WARNING", message)
}

fun logE(message: String) {
    Log.e("ERROR", message)
}