package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.*
import kotlinx.coroutines.experimental.future.*
import me.avo.kumo.lingq.*
import me.avo.kumo.nhk.data.*
import me.avo.kumo.nhk.pages.*
import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.nhk.processing.*
import me.avo.kumo.util.*

class Crawler(collection: String, val useApi: Boolean, kodein: Kodein) {

    private val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    private val database: NhkDatabase = kodein.instance()
    private val lingq = Lingq(collection, database)
    private val tagger: ArticleTagger = kodein.instance()
    private val archive: FileArchive = kodein.instance()

    fun fetchAndImport() = fetchArticles()
        .let(database::filterImported)
        .also { logger.info("Found ${it.size} articles that have not been imported yet") }
        .map(tagger::tag)
        .also(archive::archive)
        .let(this::import)

    fun import(articles: List<Article>) = when {
        articles.isEmpty() -> Unit

        useApi -> articles
            .map(Article::toLesson)
            .forEach(LingqApi::postLesson)

        else -> lingq.import(articles)
    }

    fun fetchArticles(): List<Article> = MainPage(mainUrl)
        .get()
        .map(::ArticlePage)
        .map { future { it.get() } }
        .map { it.join() }

    private val logger = this::class.getLogger()

}