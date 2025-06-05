/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.util.author
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun LicensesItem(
    library: Library,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(vertical = 12.dp, horizontal = 18.dp)
    ) {
        Row {
            StyledText(
                text = library.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (library.openSource) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.error
                ),
                maxLines = 2
            )

            if (library.artifactVersion != null || !library.openSource) {
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.End) {
                    library.artifactVersion?.let {
                        StyledText(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                    if (!library.openSource) {
                        StyledText(
                            text = stringResource(id = R.string.error_closed_source),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }

        if (library.author.isNotBlank()) {
            StyledText(
                text = library.author,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 2
            )
        }
        if (library.licenses.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                library.licenses.forEach {
                    StyledText(
                        text = it.name,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(100)
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSecondary
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}