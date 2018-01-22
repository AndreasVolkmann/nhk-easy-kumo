package me.avo.kumo.nhk.processing

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import me.avo.kumo.nhk.*
import me.avo.kumo.nhk.data.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*

internal class ArticleTaggerTest {

    private val tagger: ArticleTagger = kodein.instance()
    private val crawler = Crawler("", false, kodein)

    @Test
    fun run() {
        val articles = crawler.fetchArticles()
        val tagged = articles.map(tagger::tag)

        tagged.forEach { println("${it.title} - ${it.tags}") }

    }

    @Test
    fun `given category`() {
        listOf("article.txt", "article_2.txt")
            .mapIndexed { i, name ->
                Article(
                    "$i", "", "", "", getResource(name), ByteArray(0), null, ByteArray(0), "", mock(), listOf(), false
                )
            }
            .map(tagger::tag)
            .forEach(::println)
    }

    fun getResource(name: String) = this::class.java.classLoader.getResource(name).readText()

}