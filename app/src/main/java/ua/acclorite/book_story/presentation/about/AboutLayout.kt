package ua.acclorite.book_story.presentation.about

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideContributorsPage
import ua.acclorite.book_story.presentation.core.constants.provideIssuesPage
import ua.acclorite.book_story.presentation.core.constants.provideReleasesPage
import ua.acclorite.book_story.presentation.core.constants.provideTranslationPage
import ua.acclorite.book_story.ui.about.AboutEvent

@Composable
fun AboutLayout(
    updateLoading: Boolean,
    paddingValues: PaddingValues,
    listState: LazyListState,
    checkForUpdate: (AboutEvent.OnCheckForUpdate) -> Unit,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateToLicenses: () -> Unit,
    navigateToCredits: () -> Unit
) {
    val context = LocalContext.current
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                description = buildAnnotatedString {
                    append(
                        stringResource(
                            id = R.string.app_version_option_desc_1,
                            context.getString(R.string.app_version)
                        )
                    )
                    append("\n")
                    append(stringResource(id = R.string.app_version_option_desc_2))
                },
                showLoading = updateLoading
            ) {
                checkForUpdate(
                    AboutEvent.OnCheckForUpdate(
                        context = context
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
                        page = Constants.provideIssuesPage(),
                        context = context
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
                        page = Constants.provideContributorsPage(),
                        context = context
                    )
                )
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.whats_new_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = Constants.provideReleasesPage(),
                        context = context
                    )
                )
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.licenses_option),
                description = null
            ) {
                navigateToLicenses()
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.credits_option),
                description = null
            ) {
                navigateToCredits()
            }
        }

        item {
            AboutItem(
                title = stringResource(id = R.string.help_translate_option),
                description = null
            ) {
                navigateToBrowserPage(
                    AboutEvent.OnNavigateToBrowserPage(
                        page = Constants.provideTranslationPage(),
                        context = context
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