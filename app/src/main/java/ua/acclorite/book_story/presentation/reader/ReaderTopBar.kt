package ua.acclorite.book_story.presentation.reader

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.settings.SettingsEvent
import ua.acclorite.book_story.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReaderTopBar(
    book: Book,
    currentChapter: Chapter?,
    fastColorPresetChange: Boolean,
    currentChapterProgress: Float,
    isLoading: Boolean,
    lockMenu: Boolean,
    leave: (ReaderEvent.OnLeave) -> Unit,
    selectPreviousPreset: (SettingsEvent.OnSelectPreviousPreset) -> Unit,
    selectNextPreset: (SettingsEvent.OnSelectNextPreset) -> Unit,
    showSettingsBottomSheet: (ReaderEvent.OnShowSettingsBottomSheet) -> Unit,
    showChaptersDrawer: (ReaderEvent.OnShowChaptersDrawer) -> Unit,
    navigateBack: () -> Unit,
    navigateToBookInfo: () -> Unit
) {
    val activity = LocalActivity.current
    val animatedChapterProgress = animateFloatAsState(
        targetValue = currentChapterProgress
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
            .readerColorPresetChange(
                colorPresetChangeEnabled = fastColorPresetChange,
                isLoading = isLoading,
                selectPreviousPreset = selectPreviousPreset,
                selectNextPreset = selectNextPreset
            )
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = R.string.go_back_content_desc,
                    disableOnClick = true
                ) {
                    leave(
                        ReaderEvent.OnLeave(
                            activity = activity,
                            navigate = {
                                navigateBack()
                            }
                        )
                    )
                }
            },
            title = {
                Text(
                    text = book.title,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .noRippleClickable(
                            enabled = !lockMenu,
                            onClick = {
                                leave(
                                    ReaderEvent.OnLeave(
                                        activity = activity,
                                        navigate = {
                                            navigateToBookInfo()
                                        }
                                    )
                                )
                            }
                        )
                        .basicMarquee(iterations = Int.MAX_VALUE))
            },
            subtitle = {
                Text(
                    text = currentChapter?.title
                        ?: activity.getString(R.string.no_chapters),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                if (currentChapter != null) {
                    IconButton(
                        icon = Icons.Rounded.Menu,
                        contentDescription = R.string.chapters_content_desc,
                        disableOnClick = false,
                        enabled = !lockMenu
                    ) {
                        showChaptersDrawer(ReaderEvent.OnShowChaptersDrawer)
                    }
                }

                IconButton(
                    icon = Icons.Default.Settings,
                    contentDescription = R.string.open_reader_settings_content_desc,
                    disableOnClick = false,
                    enabled = !lockMenu
                ) {
                    showSettingsBottomSheet(ReaderEvent.OnShowSettingsBottomSheet)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (currentChapter != null) {
            LinearProgressIndicator(
                progress = { animatedChapterProgress.value },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}