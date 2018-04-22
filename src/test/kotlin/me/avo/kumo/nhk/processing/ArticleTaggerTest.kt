package me.avo.kumo.nhk.processing

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import me.avo.kumo.nhk.data.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import java.io.File

internal class ArticleTaggerTest {

    private val tagger: ArticleTagger = kodein.instance()
    private val tokenizer: ArticleTokenizer = kodein.instance()

    @TestFactory
    fun `articles should have expected tags`() = readTsv().mapIndexed { i, (article, expectedTag) ->
        dynamicTest("Expect correct tag $i: $expectedTag") { article.tags shouldContain expectedTag }
    }

    @Test
    fun `analyze failed`() {
        readTsv()
            .take(4)
            .associate { it.first to it.second }
            .mapKeys { tokenizer.tokenize(it.key.content) }

    }

    private fun readTsv(): List<Pair<Article, String>> = getResource("articles.tsv")
        .split("\r\n")
        .map(String::trim)
        .filter(String::isNotBlank)
        .drop(1)
        .map { it.split("|") }
        .map { it[0] to it[1] }
        .mapIndexed { i, (tag, content) ->
            Article(
                "$i", "", "", "", content, ByteArray(0), null, File(""), "", mock(), listOf(), false
            ) to tag
        }
        .toMap()
        .map { tagger.tag(it.key) to it.value }


    private fun getResource(name: String) = this::class.java.classLoader.getResource(name).readText()

}