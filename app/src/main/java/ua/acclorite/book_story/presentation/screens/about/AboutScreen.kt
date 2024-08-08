package ua.acclorite.book_story.presentation.screens.about

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.about.components.AboutItem
import ua.acclorite.book_story.presentation.screens.about.components.AboutUpdateDialog
import ua.acclorite.book_story.presentation.screens.about.components.badges.AboutBadges
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.data.AboutState
import ua.acclorite.book_story.presentation.screens.about.data.AboutViewModel

@Composable
fun AboutScreenRoot() {
    val navigator = LocalNavigator.current
    val aboutViewModel: AboutViewModel = hiltViewModel()

    val state = aboutViewModel.state.collectAsState()

    AboutScreen(
        state = state,
        onNavigate = { navigator.it() },
        onEvent = aboutViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen(
    state: State<AboutState>,
    onNavigate: OnNavigate,
    onEvent: (AboutEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()

    if (state.value.showUpdateDialog) {
        AboutUpdateDialog(
            state = state,
            onEvent = onEvent
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollState.first.nestedScrollConnection)
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
                scrollBehavior = scrollState.first,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = scrollState.second
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
                        painterResource(id = R.drawable.app_icon_monochrome),
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
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.no_updates),
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                error = {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_check_internet),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_updates),
                            Toast.LENGTH_LONG
                        ).show()
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
                            context.getString(R.string.issues_page),
                            context,
                            noAppsFound = {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_browser),
                                    Toast.LENGTH_SHORT
                                ).show()
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
                            context.getString(R.string.releases_page),
                            context,
                            noAppsFound = {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_browser),
                                    Toast.LENGTH_SHORT
                                ).show()
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
                    title = stringResource(id = R.string.support_option),
                    description = null
                ) {
                    onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            context.getString(R.string.support_page),
                            context,
                            noAppsFound = {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_browser),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    )
                }
            }

            item {
                AboutBadges(onEvent = onEvent)
            }
        }
    }
}