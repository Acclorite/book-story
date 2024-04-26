package ua.acclorite.book_story.presentation.screens.about.nested.license_info

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoEvent
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoState
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoViewModel

@Composable
fun LicenseInfoScreenRoot() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    val licenseInfoViewModel: LicenseInfoViewModel = hiltViewModel()

    val state = licenseInfoViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        licenseInfoViewModel.init(
            navigator,
            context
        )
    }

    LicenseInfoScreen(
        state = state,
        onEvent = licenseInfoViewModel::onEvent,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseInfoScreen(
    state: State<LicenseInfoState>,
    onEvent: (LicenseInfoEvent) -> Unit,
    navigator: Navigator
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()
    val context = LocalContext.current

    if (state.value.license != null) {
        val licenses = remember(state.value) {
            state.value.license!!.licenses.toList()
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
                        Text(
                            state.value.license!!.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        GoBackButton(navigator = navigator)
                    },
                    actions = {
                        if (state.value.license?.website?.isNotBlank() == true) {
                            CustomIconButton(
                                icon = Icons.Outlined.Language,
                                contentDescription = R.string.open_in_web_content_desc,
                                disableOnClick = false
                            ) {
                                var url = state.value.license?.website!!
                                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                    url = "https://$url"
                                }

                                onEvent(
                                    LicenseInfoEvent.OnOpenLicensePage(
                                        page = url,
                                        context = context,
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
                state = scrollState.second,
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                customItems(licenses, key = { it.name }) { license ->
                    val showed = remember {
                        mutableStateOf(licenses.size == 1)
                    }

                    Column(
                        modifier = Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null
                        )
                    ) {
                        Text(
                            text = license.name,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .clickable(enabled = license.licenseContent?.isNotBlank() == true) {
                                    showed.value = !showed.value
                                }
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                        )

                        CustomAnimatedVisibility(
                            visible = showed.value && license.licenseContent?.isNotBlank() == true,
                            enter = slideInVertically(tween(300)) { -50 } +
                                    fadeIn(tween(300)),
                            exit = slideOutVertically(tween(200)) { -30 } +
                                    fadeOut(tween(150)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = license.licenseContent!!
                                        .lines()
                                        .joinToString(separator = "\n") {
                                            it.trim()
                                        },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.loading),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.55f),
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
            )
        }
    }
}