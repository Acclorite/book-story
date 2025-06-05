/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.reader.ReaderText.Chapter
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawer
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawerSelectableItem
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawerTitleItem
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.reader.model.ExpandableChapter
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun ReaderChaptersDrawer(
    show: Boolean,
    chapters: List<Chapter>,
    currentChapter: Chapter?,
    currentChapterProgress: Float,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit
) {
    val expandableChapters = remember(show, chapters, currentChapter) {
        mutableStateListOf<ExpandableChapter>().apply {
            var index = 0
            while (index < chapters.size) {
                val chapter = chapters.getOrNull(index) ?: continue
                when (chapter.nested) {
                    false -> {
                        val children = chapters.drop(index + 1).takeWhile { it.nested }
                        add(
                            ExpandableChapter(
                                parent = chapter,
                                expanded = chapter.id == currentChapter?.id ||
                                        children.any { it.id == currentChapter?.id },
                                chapters = children.takeIf { it.isNotEmpty() }
                            )
                        )
                        index += children.size + 1
                    }

                    true -> {
                        add(
                            ExpandableChapter(
                                parent = chapter.copy(nested = false),
                                expanded = false,
                                chapters = null
                            )
                        )
                        index++
                    }
                }
            }
        }
    }

    ModalDrawer(
        show = show,
        startIndex = chapters.indexOf(currentChapter).takeIf { it != -1 } ?: 0,
        onDismissRequest = { dismissDrawer(ReaderEvent.OnDismissDrawer) },
        header = {
            ModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        expandableChapters.forEach { expandableChapter ->
            item {
                ModalDrawerSelectableItem(
                    selected = expandableChapter.parent.id == currentChapter?.id,
                    onClick = {
                        scrollToChapter(
                            ReaderEvent.OnScrollToChapter(
                                chapter = expandableChapter.parent
                            )
                        )
                        dismissDrawer(ReaderEvent.OnDismissDrawer)
                    }
                ) {
                    StyledText(
                        text = expandableChapter.parent.title,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    if (expandableChapter.parent == currentChapter) {
                        Spacer(modifier = Modifier.width(18.dp))
                        StyledText(text = "${currentChapterProgress.calculateProgress(0)}%")
                    }

                    if (!expandableChapter.chapters.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(18.dp))
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropUp,
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    expandableChapters.indexOf(expandableChapter)
                                        .also { chapterIndex ->
                                            if (chapterIndex == -1) return@noRippleClickable
                                            expandableChapters[chapterIndex] =
                                                expandableChapter.copy(
                                                    expanded = !expandableChapter.expanded
                                                )
                                        }
                                }
                                .rotate(
                                    animateFloatAsState(
                                        targetValue = if (expandableChapter.expanded) 0f else -180f
                                    ).value
                                ),
                            contentDescription = stringResource(
                                id = if (expandableChapter.expanded) R.string.collapse_content_desc
                                else R.string.expand_content_desc
                            )
                        )
                    }
                }
            }

            if (!expandableChapter.chapters.isNullOrEmpty()) {
                items(expandableChapter.chapters) { chapter ->
                    ExpandingTransition(visible = expandableChapter.expanded) {
                        ModalDrawerSelectableItem(
                            selected = chapter.id == currentChapter?.id,
                            onClick = {
                                scrollToChapter(
                                    ReaderEvent.OnScrollToChapter(
                                        chapter = chapter
                                    )
                                )
                                dismissDrawer(ReaderEvent.OnDismissDrawer)
                            }
                        ) {
                            Spacer(modifier = Modifier.width(18.dp))

                            StyledText(
                                text = chapter.title,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )

                            if (chapter == currentChapter) {
                                Spacer(modifier = Modifier.width(18.dp))
                                StyledText(text = "${currentChapterProgress.calculateProgress(0)}%")
                            }
                        }
                    }
                }
            }
        }
    }
}