/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
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
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.ChapterId
import ua.acclorite.book_story.domain.model.book.cleanTitle
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawer
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawerSelectableItem
import ua.acclorite.book_story.ui.common.components.modal_drawer.ModalDrawerTitleItem
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.reader.model.ExpandableChapter

private fun Chapter.getAllDescendantIds(childrenMap: Map<ChapterId?, List<Chapter>>): Set<ChapterId> {
    val result = mutableSetOf<ChapterId>()
    fun collect(parentId: ChapterId) {
        val children = childrenMap[parentId] ?: emptyList()
        children.forEach { child ->
            result.add(child.id)
            collect(child.id)
        }
    }
    collect(id)
    return result
}

@Composable
fun ReaderChaptersDrawer(
    show: Boolean,
    chapters: List<Chapter>,
    currentChapter: Chapter?,
    currentChapterProgress: Float,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit
) {
    val chapterMap = remember(chapters) { chapters.associateBy { it.id } }
    val childrenMap = remember(chapters) { chapters.groupBy { it.parentId } }

    val expandedIds = remember(show, chapters, currentChapter) {
        mutableStateSetOf<ChapterId>().apply {
            if (currentChapter != null) {
                var curr: Chapter? = currentChapter
                while (curr?.parentId != null) {
                    val parent = chapterMap[curr.parentId] ?: break
                    add(parent.id)
                    curr = parent
                }
            }
        }
    }

    val visibleChapters = remember(chapters, expandedIds.toList()) {
        val rootChapters = chapters.filter { it.parentId == null || it.parentId !in chapterMap }
        val result = mutableListOf<ExpandableChapter>()

        fun traverse(chapter: Chapter, depth: Int) {
            val children = childrenMap[chapter.id] ?: emptyList()
            val hasChildren = children.isNotEmpty()
            val isExpanded = expandedIds.contains(chapter.id)

            result.add(
                ExpandableChapter(
                    chapter = chapter,
                    depth = depth,
                    hasChildren = hasChildren,
                    expanded = isExpanded
                )
            )

            if (hasChildren && isExpanded) {
                children.forEach { child ->
                    traverse(child, depth + 1)
                }
            }
        }

        rootChapters.forEach { root ->
            traverse(root, depth = 0)
        }

        result
    }

    ModalDrawer(
        show = show,
        startIndex = visibleChapters.indexOfFirst { it.chapter.id == currentChapter?.id }
            .takeIf { it != -1 } ?: 0,
        onDismissRequest = { dismissDrawer(ReaderEvent.OnDismissDrawer) },
        header = {
            ModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        items(
            items = visibleChapters,
            key = { it.chapter.id.id }
        ) { item ->
            val chapter = item.chapter
            val isSelected = chapter.id == currentChapter?.id

            ModalDrawerSelectableItem(
                selected = isSelected,
                onClick = {
                    scrollToChapter(
                        ReaderEvent.OnScrollToChapter(
                            chapter = chapter
                        )
                    )
                    dismissDrawer(ReaderEvent.OnDismissDrawer)
                }
            ) {
                if (item.depth > 0) {
                    Spacer(modifier = Modifier.width((item.depth * 8).dp))
                }

                StyledText(
                    text = chapter.cleanTitle,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                if (isSelected) {
                    Spacer(modifier = Modifier.width(18.dp))
                    StyledText(text = "${currentChapterProgress.calculateProgress(0)}%")
                }

                if (item.hasChildren) {
                    Spacer(modifier = Modifier.width(18.dp))
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropUp,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable {
                                val descendants = chapter.getAllDescendantIds(childrenMap)
                                if (expandedIds.contains(chapter.id)) {
                                    expandedIds.remove(chapter.id)
                                    expandedIds.removeAll(descendants)
                                } else {
                                    expandedIds.add(chapter.id)
                                    expandedIds.addAll(descendants)
                                }
                            }
                            .rotate(
                                animateFloatAsState(
                                    targetValue = if (item.expanded) 0f else -180f,
                                    label = "chapter_arrow_rotation"
                                ).value
                            ),
                        contentDescription = stringResource(
                            id = if (item.expanded) R.string.collapse_content_desc
                            else R.string.expand_content_desc
                        )
                    )
                }
            }
        }
    }
}
