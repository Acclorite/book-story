package ua.acclorite.book_story.presentation.help

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.navigator.NavigatorBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpTopBar(
    fromStart: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToStart: () -> Unit,
    navigateBack: () -> Unit
) {
    LargeTopAppBar(
        title = {
            Text(stringResource(id = R.string.help_screen))
        },
        navigationIcon = {
            if (!fromStart) NavigatorBackIconButton(navigateBack = navigateBack)
        },
        actions = {
            if (!fromStart) {
                IconButton(
                    icon = Icons.Outlined.RestartAlt,
                    contentDescription = R.string.reset_start_content_desc,
                    disableOnClick = false
                ) {
                    navigateToStart()
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}