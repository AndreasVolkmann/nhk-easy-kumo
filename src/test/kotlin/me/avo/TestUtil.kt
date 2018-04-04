package me.avo

import me.avo.kumo.util.loadResource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object TestUtil

fun parseHtml(html: String): Document = Jsoup.parse(html)

fun getBodyFromHtmlFile(name: String): Element = TestUtil::class.loadResource(name).let(::parseHtml).body()


