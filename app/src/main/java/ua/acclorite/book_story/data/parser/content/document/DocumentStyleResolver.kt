/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.content.document

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator
import org.jsoup.select.QueryParser

private val cssComments = Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL)
private val cssRules = Regex("([^{}]+)\\{([^{}]*)\\}")
private val cssIdSpecificity = Regex("#")
private val cssClassSpecificity = Regex("[.\\[:]")
private val cssTagSpecificity = Regex("(^|[\\s>+~])\\w+")

internal class DocumentStyleResolver(document: Document, stylesheets: List<String>) {
    private data class Declaration(val value: String, val important: Boolean)
    private data class Rule(val selector: Evaluator, val declarations: Map<String, Declaration>, val specificity: Int)
    private val semanticProperties = setOf(
        "font-weight", "font-style", "font-family", "font-size",
        "text-decoration", "text-decoration-line", "vertical-align", "display", "visibility"
    )
    private val rules: List<Rule>
    private val cache = java.util.IdentityHashMap<Element, Map<String, String>>()

    init {
        val embedded = document.getAllElements().filter {
            val tag = it.normalName().substringAfter(':')
            (tag == "style" && !it.hasAttr("name")) ||
                (tag == "stylesheet" && it.attr("type").equals("text/css", ignoreCase = true))
        }.map { it.data().ifBlank { it.wholeText() } }
        rules = (stylesheets + embedded).flatMap { sheet ->
            val css = removeAtRules(sheet.replace(cssComments, ""))
            cssRules.findAll(css).flatMap { match ->
                val properties = declarations(match.groupValues[2]).filterKeys { it in semanticProperties }
                match.groupValues[1].split(',').asSequence().mapNotNull { selector ->
                    val value = selector.trim()
                    if (value.isEmpty() || properties.isEmpty()) return@mapNotNull null
                    val evaluator = runCatching { QueryParser.parse(value) }.getOrNull()
                        ?: return@mapNotNull null
                    Rule(
                        evaluator, properties,
                        cssIdSpecificity.findAll(value).count() * 100 +
                            cssClassSpecificity.findAll(value).count() * 10 +
                            cssTagSpecificity.findAll(value).count()
                    )
                }
            }.toList()
        }
    }

    fun resolve(element: Element): Map<String, String> = cache.getOrPut(element) {
        val result = mutableMapOf<String, Pair<Int, String>>()
        rules.forEach { rule ->
            if (runCatching { element.`is`(rule.selector) }.getOrDefault(false)) {
                rule.declarations.forEach { (property, declaration) ->
                    val priority = rule.specificity + if (declaration.important) 1_000_000 else 0
                    if (priority >= (result[property]?.first ?: -1)) result[property] = priority to declaration.value
                }
            }
        }
        declarations(element.attr("style")).forEach { (property, declaration) ->
            val priority = 100_000 + if (declaration.important) 1_000_000 else 0
            if (priority >= (result[property]?.first ?: -1)) result[property] = priority to declaration.value
        }
        result.mapValues { it.value.second }
    }

    private fun declarations(value: String): Map<String, Declaration> {
        val result = mutableMapOf<String, Declaration>()
        value.split(';').forEach {
            val separator = it.indexOf(':')
            if (separator >= 0) {
                val property = it.substring(0, separator).trim().lowercase()
                val raw = it.substring(separator + 1).trim().lowercase()
                val declaration = Declaration(raw.substringBefore("!important").trim(), "!important" in raw)
                if (result[property]?.important != true || declaration.important) result[property] = declaration
            }
        }
        return result
    }

    private fun removeAtRules(css: String): String {
        val output = StringBuilder()
        var index = 0
        while (index < css.length) {
            if (css[index] != '@') {
                output.append(css[index++])
                continue
            }
            while (index < css.length && css[index] != '{' && css[index] != ';') index++
            if (index < css.length && css[index] == '{') {
                var depth = 1
                index++
                while (index < css.length && depth > 0) {
                    if (css[index] == '{') depth++
                    if (css[index] == '}') depth--
                    index++
                }
            } else if (index < css.length) index++
        }
        return output.toString()
    }
}
