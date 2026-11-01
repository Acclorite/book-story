/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.book

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
data class Node(
    val id: NodeId,
    val position: NodePosition,
    val content: NodeContent
)

@JvmInline
@Parcelize
value class NodeId(val id: Int) : Parcelable

@Immutable
data class NodePosition(
    val length: Int,
    val offset: Int
) {
    val endOffset = offset + length
}

@Immutable
sealed class NodeContent {
    fun computeLength(): Int {
        return when (this) {
            is Header -> title.length
            is Image -> 1
            is TextBlock -> if (this is TableRow) text.length.coerceAtLeast(1) else text.length
            is Separator -> 1
        }
    }

    enum class HeaderLevel(val level: Int) {
        H1(1), H2(2), H3(3), H4(4), H5(5), H6(6);

        companion object {
            fun forNumber(level: Int): HeaderLevel {
                return when (level) {
                    1 -> H1
                    2 -> H2
                    3 -> H3
                    4 -> H4
                    5 -> H5
                    6 -> H6
                    else -> throw IllegalArgumentException("Incorrect value $level for HeaderLevel.")
                }
            }

            fun isHigherOrEqual(header1: HeaderLevel, header2: HeaderLevel) =
                header1.level <= header2.level

            fun isHigher(header1: HeaderLevel, header2: HeaderLevel) = header1.level < header2.level
        }
    }

    @Immutable
    sealed class TextStyle {
        @Immutable
        data object Bold : TextStyle()

        @Immutable
        data object Italic : TextStyle()

        @Immutable
        data object Underline : TextStyle()
        @Immutable
        data object Strikethrough : TextStyle()
        @Immutable
        data object Superscript : TextStyle()
        @Immutable
        data object Subscript : TextStyle()
        @Immutable
        data object Monospace : TextStyle()
        @Immutable
        data object Small : TextStyle()
        @Immutable
        data object Highlight : TextStyle()

        @Immutable
        data class Link(val link: String) : TextStyle()
    }

    @Immutable
    data class TextStyleRange(
        val start: Int,
        val end: Int,
        val style: TextStyle
    )

    @Immutable
    data class Header(
        val title: String,
        val level: HeaderLevel
    ) : NodeContent()

    @Immutable
    sealed class TextBlock(open val text: String, open val styles: List<TextStyleRange>) : NodeContent()

    @Immutable
    data class Paragraph(
        override val text: String,
        override val styles: List<TextStyleRange>
    ) : TextBlock(text, styles)

    @Immutable
    data class Quote(
        override val text: String,
        override val styles: List<TextStyleRange>,
        val depth: Int = 1
    ) : TextBlock(text, styles)

    @Immutable
    data class CodeBlock(
        override val text: String,
        override val styles: List<TextStyleRange>
    ) : TextBlock(text, styles)

    @Immutable
    data class Verse(
        override val text: String,
        override val styles: List<TextStyleRange>
    ) : TextBlock(text, styles)

    @Immutable
    data class ListItem(
        override val text: String,
        override val styles: List<TextStyleRange>,
        val marker: String = "•",
        val depth: Int = 1
    ) : TextBlock(text, styles)

    @Immutable
    data class Subtitle(
        override val text: String,
        override val styles: List<TextStyleRange>
    ) : TextBlock(text, styles)

    @Immutable
    data class TableCell(
        val start: Int,
        val end: Int,
        val column: Int,
        val columnSpan: Int = 1,
        val rowSpan: Int = 1,
        val header: Boolean = false,
        val continuation: Boolean = false
    )

    @Immutable
    data class TableRow(
        override val text: String,
        override val styles: List<TextStyleRange>,
        val header: Boolean = false,
        val cells: List<TableCell> = emptyList(),
        val columns: Int = 0,
        val firstRow: Boolean = true,
        val lastRow: Boolean = true
    ) : TextBlock(text, styles)

    @Immutable
    data class Caption(
        override val text: String,
        override val styles: List<TextStyleRange>
    ) : TextBlock(text, styles)

    @Immutable
    data class Image(
        val content: String,
        val caption: String?,
        val width: Int,
        val height: Int,
        val aspectRatio: Float
    ) : NodeContent()

    @Immutable
    data object Separator : NodeContent()
}