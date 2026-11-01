/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.node_content

import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.proto.HeaderLevelProto
import ua.acclorite.book_story.proto.HeaderProto
import ua.acclorite.book_story.proto.ImageProto
import ua.acclorite.book_story.proto.NodeContentListProto
import ua.acclorite.book_story.proto.NodeContentProto
import ua.acclorite.book_story.proto.ParagraphProto
import ua.acclorite.book_story.proto.SeparatorProto
import ua.acclorite.book_story.proto.TextBlockKindProto
import ua.acclorite.book_story.proto.TextBlockProto
import ua.acclorite.book_story.proto.TextStyleProto
import ua.acclorite.book_story.proto.textBlockProto
import ua.acclorite.book_story.proto.tableCellProto
import ua.acclorite.book_story.proto.headerProto
import ua.acclorite.book_story.proto.imageProto
import ua.acclorite.book_story.proto.nodeContentListProto
import ua.acclorite.book_story.proto.nodeContentProto
import ua.acclorite.book_story.proto.paragraphProto
import ua.acclorite.book_story.proto.separatorProto
import ua.acclorite.book_story.proto.textStyleProto
import ua.acclorite.book_story.proto.textStyleRangeProto
import javax.inject.Inject

class NodeContentMapperImpl @Inject constructor() : NodeContentMapper {
    private fun toHeaderProto(header: NodeContent.Header): HeaderProto {
        return headerProto {
            title = header.title
            level = HeaderLevelProto.forNumber(header.level.level)
        }
    }

    private fun toHeader(headerProto: HeaderProto): NodeContent.Header {
        return NodeContent.Header(
            title = headerProto.title,
            level = NodeContent.HeaderLevel.forNumber(headerProto.level.number)
        )
    }

    private fun toParagraphProto(paragraph: NodeContent.TextBlock): ParagraphProto {
        return paragraphProto {
            text = paragraph.text
            styles.addAll(
                paragraph.styles.map { textStyleRange ->
                    textStyleRangeProto {
                        start = textStyleRange.start
                        end = textStyleRange.end
                        style = textStyleProto {
                            when (textStyleRange.style) {
                                is NodeContent.TextStyle.Bold -> bold = true
                                is NodeContent.TextStyle.Italic -> italic = true
                                is NodeContent.TextStyle.Underline -> underline = true
                                is NodeContent.TextStyle.Strikethrough -> strikethrough = true
                                is NodeContent.TextStyle.Superscript -> superscript = true
                                is NodeContent.TextStyle.Subscript -> subscript = true
                                is NodeContent.TextStyle.Monospace -> monospace = true
                                is NodeContent.TextStyle.Small -> small = true
                                is NodeContent.TextStyle.Highlight -> highlight = true
                                is NodeContent.TextStyle.Link -> link = textStyleRange.style.link
                            }
                        }
                    }
                }
            )
        }
    }

    private fun toParagraph(paragraphProto: ParagraphProto): NodeContent.Paragraph {
        return NodeContent.Paragraph(
            text = paragraphProto.text,
            styles = paragraphProto.stylesList.mapNotNull { textStyleRangeProto ->
                val start = textStyleRangeProto.start.coerceIn(0, paragraphProto.text.length)
                val end = textStyleRangeProto.end.coerceIn(0, paragraphProto.text.length)
                if (start >= end) return@mapNotNull null
                NodeContent.TextStyleRange(
                    start = start,
                    end = end,
                    style = when (textStyleRangeProto.style.styleCase) {
                        TextStyleProto.StyleCase.BOLD -> NodeContent.TextStyle.Bold
                        TextStyleProto.StyleCase.ITALIC -> NodeContent.TextStyle.Italic
                        TextStyleProto.StyleCase.UNDERLINE -> NodeContent.TextStyle.Underline
                        TextStyleProto.StyleCase.STRIKETHROUGH -> NodeContent.TextStyle.Strikethrough
                        TextStyleProto.StyleCase.SUPERSCRIPT -> NodeContent.TextStyle.Superscript
                        TextStyleProto.StyleCase.SUBSCRIPT -> NodeContent.TextStyle.Subscript
                        TextStyleProto.StyleCase.MONOSPACE -> NodeContent.TextStyle.Monospace
                        TextStyleProto.StyleCase.SMALL -> NodeContent.TextStyle.Small
                        TextStyleProto.StyleCase.HIGHLIGHT -> NodeContent.TextStyle.Highlight
                        TextStyleProto.StyleCase.LINK -> NodeContent.TextStyle.Link(
                            textStyleRangeProto.style.link
                        )

                        else -> return@mapNotNull null
                    }
                )
            }
        )
    }

    private fun toTextBlockProto(block: NodeContent.TextBlock): TextBlockProto = textBlockProto {
        content = toParagraphProto(block)
        kind = when (block) {
            is NodeContent.Quote -> TextBlockKindProto.QUOTE
            is NodeContent.CodeBlock -> TextBlockKindProto.CODE_BLOCK
            is NodeContent.Verse -> TextBlockKindProto.VERSE
            is NodeContent.ListItem -> TextBlockKindProto.LIST_ITEM
            is NodeContent.Subtitle -> TextBlockKindProto.SUBTITLE
            is NodeContent.TableRow -> TextBlockKindProto.TABLE_ROW
            is NodeContent.Caption -> TextBlockKindProto.CAPTION
            is NodeContent.Paragraph -> error("Paragraph uses its existing protobuf field.")
        }
        if (block is NodeContent.Quote) depth = block.depth.coerceAtLeast(1)
        if (block is NodeContent.ListItem) {
            depth = block.depth.coerceAtLeast(1)
            marker = block.marker
        }
        if (block is NodeContent.TableRow) {
            header = block.header
            columns = block.columns
            firstRow = block.firstRow
            lastRow = block.lastRow
            cells.addAll(block.cells.map { cell ->
                tableCellProto {
                    start = cell.start
                    end = cell.end
                    column = cell.column
                    columnSpan = cell.columnSpan
                    rowSpan = cell.rowSpan
                    header = cell.header
                    continuation = cell.continuation
                }
            })
        }
    }

    private fun toTextBlock(proto: TextBlockProto): NodeContent.TextBlock {
        val paragraph = toParagraph(proto.content)
        val text = paragraph.text
        val styles = paragraph.styles
        return when (proto.kind) {
            TextBlockKindProto.QUOTE -> NodeContent.Quote(text, styles, proto.depth.coerceIn(1, 12))
            TextBlockKindProto.CODE_BLOCK -> NodeContent.CodeBlock(text, styles)
            TextBlockKindProto.VERSE -> NodeContent.Verse(text, styles)
            TextBlockKindProto.LIST_ITEM -> NodeContent.ListItem(text, styles, proto.marker, proto.depth.coerceIn(1, 12))
            TextBlockKindProto.SUBTITLE -> NodeContent.Subtitle(text, styles)
            TextBlockKindProto.TABLE_ROW -> NodeContent.TableRow(
                text, styles, proto.header,
                cells = proto.cellsList.map { cell ->
                    val start = cell.start.coerceIn(0, text.length)
                    NodeContent.TableCell(
                        start, cell.end.coerceIn(start, text.length),
                        cell.column.coerceIn(0, 999),
                        cell.columnSpan.coerceIn(1, 1000 - cell.column.coerceIn(0, 999)),
                        cell.rowSpan.coerceIn(1, 65534),
                        cell.header, cell.continuation
                    )
                },
                columns = proto.columns.coerceIn(0, 1000),
                firstRow = proto.cellsCount == 0 || proto.firstRow,
                lastRow = proto.cellsCount == 0 || proto.lastRow
            )
            TextBlockKindProto.CAPTION -> NodeContent.Caption(text, styles)
            else -> paragraph
        }
    }

    private fun toImageProto(image: NodeContent.Image): ImageProto {
        return imageProto {
            content = image.content
            caption = image.caption ?: ""
            width = image.width
            height = image.height
            aspectRatio = image.aspectRatio
        }
    }

    private fun toImage(imageProto: ImageProto): NodeContent.Image {
        return NodeContent.Image(
            content = imageProto.content,
            caption = imageProto.caption.takeIf { it.isNotBlank() },
            width = imageProto.width,
            height = imageProto.height,
            aspectRatio = imageProto.aspectRatio
        )
    }

    private fun toSeparatorProto(separator: NodeContent.Separator): SeparatorProto {
        return separatorProto { }
    }

    private fun toSeparator(separatorProto: SeparatorProto): NodeContent.Separator {
        return NodeContent.Separator
    }

    override fun toNodeContentProto(nodeContent: NodeContent): NodeContentProto {
        return nodeContentProto {
            when (nodeContent) {
                is NodeContent.Header -> header = toHeaderProto(nodeContent)
                is NodeContent.Paragraph -> paragraph = toParagraphProto(nodeContent)
                is NodeContent.TextBlock -> textBlock = toTextBlockProto(nodeContent)
                is NodeContent.Image -> image = toImageProto(nodeContent)
                is NodeContent.Separator -> separator = toSeparatorProto(nodeContent)
            }
        }
    }

    override fun toNodeContent(nodeContentProto: NodeContentProto): NodeContent {
        return when (nodeContentProto.contentCase) {
            NodeContentProto.ContentCase.HEADER -> toHeader(nodeContentProto.header)
            NodeContentProto.ContentCase.PARAGRAPH -> toParagraph(nodeContentProto.paragraph)
            NodeContentProto.ContentCase.TEXT_BLOCK -> toTextBlock(nodeContentProto.textBlock)
            NodeContentProto.ContentCase.IMAGE -> toImage(nodeContentProto.image)
            NodeContentProto.ContentCase.SEPARATOR -> toSeparator(nodeContentProto.separator)
            else -> throw IllegalStateException("Incorrect proto contentCase: ${nodeContentProto.contentCase}")
        }
    }

    override fun toNodeContentListProto(nodeContentList: List<NodeContent>): NodeContentListProto {
        return nodeContentListProto {
            nodes.addAll(nodeContentList.map { toNodeContentProto(it) })
        }
    }

    override fun toNodeContentList(nodeContentListProto: NodeContentListProto): List<NodeContent> {
        return nodeContentListProto.nodesList.map { toNodeContent(it) }
    }
}