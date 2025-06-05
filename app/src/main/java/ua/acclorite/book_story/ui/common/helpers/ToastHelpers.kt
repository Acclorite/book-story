/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.helpers

import android.content.Context
import android.widget.Toast

var toast: Toast? = null

fun String.showToast(context: Context, longToast: Boolean = true) {
    toast?.cancel()
    toast = null

    toast = Toast.makeText(context, this, if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}