/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.core.util

fun <T> MutableList<T>.addAll(calculation: () -> List<T>) {
    addAll(calculation())
}