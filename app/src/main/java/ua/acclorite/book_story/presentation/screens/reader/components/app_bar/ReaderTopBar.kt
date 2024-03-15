package ua.acclorite.book_story.presentation.screens.reader.components.app_bar

import androidx.activity.ComponentActivity
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.data.removeDigits
import ua.acclorite.book_story.presentation.data.removeTrailingZero
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel

/**
 * Reader top bar. Displays title of the book.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(viewModel: ReaderViewModel, navigator: Navigator, containerColor: Color) {
    val context = LocalContext.current as ComponentActivity
    val state by viewModel.state.collectAsState()

    val progress by remember(state.book.progress) {
        derivedStateOf {
            (state.book.progress * 100)
                .toDouble()
                .removeDigits(2)
                .removeTrailingZero()
                .dropWhile { it == '-' } + "%"
        }
    }

    TopAppBar(
        navigationIcon = {
            GoBackButton(navigator = navigator) {
                viewModel.onEvent(ReaderEvent.OnShowSystemBars(context))
            }
        },
        title = {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    state.book.title,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                navigator.navigateWithoutBackStack(
                                    Screen.BOOK_INFO,
                                    true,
                                    Argument(
                                        "book",
                                        state.book
                                    )
                                )
                            }
                        )
                        .basicMarquee(
                            delayMillis = 4000
                        )
                )
                Text(
                    stringResource(
                        id = R.string.read_query,
                        progress
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        actions = {
            CustomIconButton(
                icon = Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.open_reader_settings_content_desc),
                disableOnClick = false
            ) {
                viewModel.onEvent(ReaderEvent.OnShowHideSettingsBottomSheet)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor
        )
    )
}