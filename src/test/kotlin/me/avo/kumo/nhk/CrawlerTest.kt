package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.instance
import me.avo.kumo.kodein
import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.processing.ArticleTagger
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class CrawlerTest {

    private val crawler = Crawler("", TODO(), false, kodein)
    private val tagger: ArticleTagger = kodein.instance()

    @Disabled
    @Test fun `verify tagging`() {

        crawler.fetchArticles()
            .map(tagger::tag)
            .onEach { println("${it.title}: ${it.tags}") }
            .map(Article::toLesson)
            .forEach(::println)

    }

}