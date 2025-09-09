/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.general.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.use_case.book.GetSyncStatisticsUseCase
import ua.blindmint.codex.domain.use_case.book.SyncStatistics
import ua.blindmint.codex.presentation.core.components.common.StyledText

@Composable
fun SyncProgressOption() {
    var syncStatistics by remember { mutableStateOf(SyncStatistics()) }
    val getSyncStatisticsUseCase = hiltViewModel<GetSyncStatisticsUseCase>()

    LaunchedEffect(Unit) {
        syncStatistics = getSyncStatisticsUseCase.execute()
    }

    SyncProgressOption(
        syncedCount = syncStatistics.syncedCount,
        syncingCount = syncStatistics.syncingCount,
        errorCount = syncStatistics.errorCount
    )
}

@Composable
private fun SyncProgressOption(
    syncedCount: Int,
    syncingCount: Int,
    errorCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            StyledText(
                text = stringResource(R.string.sync_progress_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SyncStatusItem(
                label = stringResource(R.string.synced_books),
                count = syncedCount,
                color = MaterialTheme.colorScheme.primary
            )
            SyncStatusItem(
                label = stringResource(R.string.syncing_books),
                count = syncingCount,
                color = MaterialTheme.colorScheme.secondary
            )
            SyncStatusItem(
                label = stringResource(R.string.sync_error_books),
                count = errorCount,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SyncStatusItem(
    label: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        StyledText(
            text = count.toString(),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = color
            )
        )
        StyledText(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}