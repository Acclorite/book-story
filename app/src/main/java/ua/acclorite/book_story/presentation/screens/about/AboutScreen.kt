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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.about.components.AboutItem
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.data.AboutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    navigator: Navigator
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        canScroll = {
            listState.canScrollForward || listState.canScrollBackward
        }
    )

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
                    GoBackButton(navigator = navigator)
                },
                scrollBehavior = scrollBehavior,
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
                        painterResource(id = R.drawable.app_icon_monochrome),
                        contentDescription = stringResource(id = R.string.app_icon_content_desc),
                        modifier = Modifier
                            .size(180.dp),
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
                    description = stringResource(
                        id = R.string.app_version_option_desc,
                        stringResource(id = R.string.app_version)
                    ),
                    isOnClickEnabled = false
                )
            }

            item {
                AboutItem(
                    title = stringResource(id = R.string.all_releases_option),
                    description = stringResource(
                        id = R.string.all_releases_option_desc
                    )
                ) {
                    Toast.makeText(
                        context,
                        "${context.getString(R.string.app_version_option)}:" +
                                " ${context.getString(R.string.app_version)}",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.onEvent(
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
                    title = stringResource(id = R.string.issues_option),
                    description = stringResource(
                        id = R.string.issues_option_desc
                    )
                ) {
                    viewModel.onEvent(
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
                    title = stringResource(id = R.string.project_page_option),
                    description = stringResource(
                        id = R.string.project_page_option_desc
                    )
                ) {
                    viewModel.onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            context.getString(R.string.project_page),
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
                    title = stringResource(id = R.string.my_page_option),
                    description = stringResource(
                        id = R.string.my_page_option_desc
                    )
                ) {
                    viewModel.onEvent(
                        AboutEvent.OnNavigateToBrowserPage(
                            context.getString(R.string.my_page),
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
                    title = stringResource(id = R.string.support_option),
                    description = stringResource(
                        id = R.string.support_option_desc
                    )
                ) {
                    viewModel.onEvent(
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
                AboutItem(
                    title = stringResource(id = R.string.slava_ukraini_option),
                    description = null
                ) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.slava_ukraini_option_desc),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}