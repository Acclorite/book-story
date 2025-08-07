/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.BuildConfig
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.data.LinkData
import ua.acclorite.book_story.presentation.about.AboutEvent
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar

@Composable
fun AboutLayout(
    paddingValues: PaddingValues,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateToLicenses: (AboutEvent.OnNavigateToLicenses) -> Unit,
    navigateToCredits: (AboutEvent.OnNavigateToCredits) -> Unit
) {
    LazyColumnWithScrollbar(
        Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        state = listState
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_icon_content_desc),
                    modifier = Modifier
                        .padding(14.dp)
                        .size(120.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.app_version_option),
                description = "${stringResource(R.string.app_name)} v${BuildConfig.VERSION_NAME}",
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = LinkData.RELEASES
                    )
                )
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.report_bug_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = LinkData.ISSUES
                    )
                )
            }
        }


        item {
            AboutItem(
                title = stringResource(id = R.string.contributors_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = LinkData.CONTRIBUTORS
                    )
                )
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.licenses_option),
                description = null
            ) {
                navigateToLicenses(AboutEvent.OnNavigateToLicenses)
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.credits_option),
                description = null
            ) {
                navigateToCredits(AboutEvent.OnNavigateToCredits)
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.help_translate_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = LinkData.TRANSLATION
                    )
                )
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.support_development_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = LinkData.SUPPORT
                    )
                )
            }
        }

        item {
            AboutBadges(
                navigateToBrowserPage = navigateToBrowserPage
            )
        }
    }
}