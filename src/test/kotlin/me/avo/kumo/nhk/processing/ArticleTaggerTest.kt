package me.avo.kumo.nhk.processing

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import me.avo.kumo.nhk.*
import org.junit.jupiter.api.*

internal class ArticleTaggerTest {

    @Test
    fun run() {

        val tagger: ArticleTagger = kodein.instance()
        val crawler = Crawler("", false, kodein.instance())

        val articles = crawler.fetchArticles()
        val tagged = articles.map(tagger::tag)

        tagged.forEach { println("${it.title} - ${it.tags}") }

    }

}