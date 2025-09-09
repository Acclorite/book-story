/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.browse.scan.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.settings.components.SettingsSubcategoryNote

@Composable
fun BrowseScanOptionNote() {
    SettingsSubcategoryNote(
        text = stringResource(
            id = R.string.browse_scan_option_note
        )
    )
}