package ua.acclorite.book_story.presentation.license_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.navigator.NavigatorBackIconButton
import ua.acclorite.book_story.ui.about.AboutEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseInfoTopBar(
    library: Library,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LargeTopAppBar(
        title = {
            Text(
                library.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            NavigatorBackIconButton(navigateBack = navigateBack)
        },
        actions = {
            if (library.website?.isNotBlank() == true) {
                IconButton(
                    icon = Icons.Outlined.Language,
                    contentDescription = R.string.open_in_web_content_desc,
                    disableOnClick = false
                ) {
                    navigateToBrowserPage(
                        AboutEvent.OnNavigateToBrowserPage(
                            page = library.website!!,
                            context = context
                        )
                    )
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