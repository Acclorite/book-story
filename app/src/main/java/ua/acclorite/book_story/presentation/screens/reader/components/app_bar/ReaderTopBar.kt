package ua.acclorite.book_story.presentation.screens.reader.components.app_bar

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Upgrade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.components.readerFastColorPresetChange
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.ui.Colors

/**
 * Reader top bar. Displays title of the book.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReaderTopBar() {
    val state = ReaderViewModel.getState()
    val mainState = MainViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()
    val context = LocalContext.current as ComponentActivity
    val onNavigate = LocalNavigator.current

    val animatedChapterProgress = animateFloatAsState(
        targetValue = state.value.currentChapterProgress
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
            .readerFastColorPresetChange(
                fastColorPresetChangeEnabled = mainState.value.fastColorPresetChange,
                isLoading = state.value.loading,
                presetChanged = {
                    context
                        .getString(
                            R.string.color_preset_selected_query,
                            it
                                .asString(context)
                                .trim()
                        )
                        .showToast(context = context, longToast = false)
                }
            )
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
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
                Text(
                    text = state.value.book.title,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(end = 8.dp)
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
                                                    Screen.BookInfo(
                                                        bookId = state.value.book.id,
                                                        startUpdate = false
                                                    ),
                                                    useBackAnimation = true,
                                                    saveInBackStack = false
                                                )
                                            }
                                        }
                                    )
                                )
                            }
                        )
                        .then(
                            if (state.value.checkingForUpdate || state.value.updateFound) Modifier
                            else Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                )
            },
            subtitle = {
                Text(
                    text = state.value.currentChapter?.title
                        ?: context.getString(R.string.no_chapters),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                AnimatedVisibility(
                    visible = state.value.checkingForUpdate || state.value.updateFound,
                    enter = fadeIn(),
                    exit = shrinkHorizontally() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.value.updateFound) {
                            IconButton(
                                icon = Icons.Rounded.Upgrade,
                                contentDescription = R.string.update_text_content_desc,
                                color = MaterialTheme.colorScheme.primary,
                                disableOnClick = false,
                                enabled = !state.value.lockMenu &&
                                        state.value.updateFound &&
                                        !state.value.checkingForUpdate
                            ) {
                                onEvent(ReaderEvent.OnShowHideUpdateDialog(true))
                            }
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(19.dp)
                                    .noRippleClickable {
                                        onEvent(ReaderEvent.OnCancelCheckTextForUpdate)
                                    },
                                strokeWidth = 2.4.dp
                            )
                        }
                    }
                }

                if (state.value.currentChapter != null) {
                    IconButton(
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

                IconButton(
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