package ua.acclorite.book_story.presentation.screens.reader.components.app_bar

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomIconButton
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.ui.Colors

/**
 * Reader top bar. Displays title of the book.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar() {
    val state = LocalReaderViewModel.current.state
    val onEvent = LocalReaderViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalOnNavigate.current

    val animatedChapterProgress = animateFloatAsState(
        targetValue = state.value.currentChapterProgress
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
    ) {
        TopAppBar(
            navigationIcon = {
                CustomIconButton(
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = R.string.go_back_content_desc,
                    disableOnClick = true
                ) {
                    onEvent(
                        ReaderEvent.OnGoBack(
                            context = context,
                            refreshList = {
                                onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                            },
                            navigate = {
                                onNavigate {
                                    navigateBack()
                                }
                            }
                        )
                    )
                }
            },
            title = {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        state.value.book.title,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier
                            .noRippleClickable(
                                enabled = !state.value.lockMenu,
                                onClick = {
                                    onEvent(
                                        ReaderEvent.OnGoBack(
                                            context = context,
                                            refreshList = {
                                                onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                                onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                            },
                                            navigate = {
                                                onNavigate {
                                                    navigate(
                                                        Screen.BookInfo(state.value.book.id),
                                                        useBackAnimation = true,
                                                        saveInBackStack = false
                                                    )
                                                }
                                            }
                                        )
                                    )
                                }
                            )
                            .basicMarquee(
                                iterations = Int.MAX_VALUE
                            )
                    )
                    Text(
                        state.value.currentChapter?.title
                            ?: context.getString(R.string.no_chapters),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            actions = {
                if (state.value.currentChapter != null) {
                    CustomIconButton(
                        icon = Icons.Rounded.Menu,
                        contentDescription = R.string.chapters_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.lockMenu &&
                                !state.value.showChaptersDrawer &&
                                !state.value.showSettingsBottomSheet
                    ) {
                        onEvent(ReaderEvent.OnShowHideChaptersDrawer(true))
                    }
                }

                CustomIconButton(
                    icon = Icons.Default.Settings,
                    contentDescription = R.string.open_reader_settings_content_desc,
                    disableOnClick = false,
                    enabled = !state.value.lockMenu &&
                            !state.value.showChaptersDrawer &&
                            !state.value.showSettingsBottomSheet
                ) {
                    onEvent(ReaderEvent.OnShowHideSettingsBottomSheet(true))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (state.value.currentChapter != null) {
            LinearProgressIndicator(
                progress = { animatedChapterProgress.value },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}