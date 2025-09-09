/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.core.util

import android.content.Context
import android.widget.Toast

var toast: Toast? = null

fun String.showToast(context: Context, longToast: Boolean = true) {
    toast?.cancel()
    toast = null

    toast = Toast.makeText(context, this, if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}