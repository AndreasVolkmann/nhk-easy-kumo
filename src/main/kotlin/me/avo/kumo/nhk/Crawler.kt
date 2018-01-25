package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.*
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

    fun import(articles: List<Article>) {
        if (articles.isEmpty()) return

        if (useApi) articles
            .map(Article::toLesson)
            .map(LingqApi::postLesson)
        else lingq.import(articles)
    }

    fun fetchArticles(): List<Article> = MainPage(mainUrl)
        .get()
        .let(ArticlePage.Companion::getArticles)

    private val logger = this::class.getLogger()

}