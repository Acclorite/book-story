package ua.acclorite.book_story.presentation.screens.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.GoBackButton
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideContributorsPage
import ua.acclorite.book_story.presentation.core.constants.provideIssuesPage
import ua.acclorite.book_story.presentation.core.constants.provideReleasesPage
import ua.acclorite.book_story.presentation.core.constants.provideTranslationPage
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.about.components.AboutItem
import ua.acclorite.book_story.presentation.screens.about.components.AboutUpdateDialog
import ua.acclorite.book_story.presentation.screens.about.components.badges.AboutBadges
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.data.AboutViewModel

@Composable
fun AboutScreenRoot() {
    AboutScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen() {
    val state = AboutViewModel.getState()
    val onEvent = AboutViewModel.getEvent()
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

    val (scrollBehavior, lazyListState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

    if (state.value.showUpdateDialog) {
        AboutUpdateDialog()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.about_screen))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = lazyListState
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
                    showLoading = state.value.updateLoading
                ) {
                    if (!state.value.alreadyCheckedForUpdates) {
                        onEvent(
                            AboutEvent.OnCheckForUpdates(
                                context = context,
                                noUpdatesFound = {
                                    context.getString(R.string.no_updates)
                                        .showToast(context = context)
                                },
                                error = {
                                    context.getString(R.string.error_check_internet)
                                        .showToast(context = context)
                                }
                            )
                        )
                    } else {
                        context.getString(R.string.no_updates)
                            .showToast(context = context)
                    }
                }
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.report_bug_option),
                    description = null
                ) {
                    onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            Constants.provideIssuesPage(),
                            context,
                            noAppsFound = {
                                context.getString(R.string.error_no_browser)
                                    .showToast(context = context, longToast = false)
                            }
                        )
                    )
                }
            }


            item {
                AboutItem(
                    title = stringResource(id = R.string.contributors_option),
                    description = null
                ) {
                    onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            Constants.provideContributorsPage(),
                            context,
                            noAppsFound = {
                                context.getString(R.string.error_no_browser)
                                    .showToast(context = context, longToast = false)
                            }
                        )
                    )
                }
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.whats_new_option),
                    description = null
                ) {
                    onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            Constants.provideReleasesPage(),
                            context,
                            noAppsFound = {
                                context.getString(R.string.error_no_browser)
                                    .showToast(context = context, longToast = false)
                            }
                        )
                    )
                }
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.licenses_option),
                    description = null
                ) {
                    onNavigate {
                        navigate(Screen.About.Licenses)
                    }
                }
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.credits_option),
                    description = null
                ) {
                    onNavigate {
                        navigate(Screen.About.Credits)
                    }
                }
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.help_translate_option),
                    description = null
                ) {
                    onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            Constants.provideTranslationPage(),
                            context,
                            noAppsFound = {
                                context.getString(R.string.error_no_browser)
                                    .showToast(context = context, longToast = false)
                            }
                        )
                    )
                }
            }

            item {
                AboutBadges()
            }
        }
    }
}