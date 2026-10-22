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
import ua.acclorite.book_story.proto.TextStyleProto
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

    private fun toParagraphProto(paragraph: NodeContent.Paragraph): ParagraphProto {
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
                NodeContent.TextStyleRange(
                    start = textStyleRangeProto.start,
                    end = textStyleRangeProto.end,
                    style = when (textStyleRangeProto.style.styleCase) {
                        TextStyleProto.StyleCase.BOLD -> NodeContent.TextStyle.Bold
                        TextStyleProto.StyleCase.ITALIC -> NodeContent.TextStyle.Italic
                        TextStyleProto.StyleCase.LINK -> NodeContent.TextStyle.Link(
                            textStyleRangeProto.style.link
                        )

                        else -> return@mapNotNull null
                    }
                )
            }
        )
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
                is NodeContent.Image -> image = toImageProto(nodeContent)
                is NodeContent.Separator -> separator = toSeparatorProto(nodeContent)
            }
        }
    }

    override fun toNodeContent(nodeContentProto: NodeContentProto): NodeContent {
        return when (nodeContentProto.contentCase) {
            NodeContentProto.ContentCase.HEADER -> toHeader(nodeContentProto.header)
            NodeContentProto.ContentCase.PARAGRAPH -> toParagraph(nodeContentProto.paragraph)
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