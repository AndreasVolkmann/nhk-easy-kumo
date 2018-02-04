package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import me.avo.kumo.nhk.data.*
import me.avo.kumo.nhk.processing.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class CrawlerTest {

    private val crawler = Crawler("", false, kodein)
    private val tagger: ArticleTagger = kodein.instance()

    @Test fun `verify tagging`() {

        crawler.fetchArticles()
            .map(tagger::tag)
            .onEach { println("${it.title}: ${it.tags}") }
            .map(Article::toLesson)
            .forEach(::println)

    }

    @Test fun fetchArticles() {

        println("Before")
        crawler.fetchArticles()
        println("After")
    }


}