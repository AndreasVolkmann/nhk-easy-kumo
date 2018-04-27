package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import me.avo.kumo.lingq.Lingq
import me.avo.kumo.lingq.LingqApi
import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.pages.ArticlePage
import me.avo.kumo.nhk.pages.MainPage
import me.avo.kumo.nhk.persistence.FileArchive
import me.avo.kumo.nhk.persistence.NhkDatabase
import me.avo.kumo.nhk.processing.ArticleTagger
import me.avo.kumo.util.getLogger

class Crawler(collection: String, val ffmepgPath: String, val useApi: Boolean, kodein: Kodein) {

    private val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    private val database: NhkDatabase = kodein.instance()
    private val lingq = Lingq(collection, database)
    private val tagger: ArticleTagger = kodein.instance()
    private val archive: FileArchive = kodein.instance()

    fun fetchAndImport() = fetchArticles()
        .let(database::filterImportedOrIgnored)
        .also { logger.info("Found ${it.size} articles that have not been imported yet") }
        .map(tagger::tag)
        .also(archive::archive)
        .let(::import)

    fun import(articles: List<Article>) = when {
        articles.isEmpty() -> Unit

        useApi -> articles
            .map(Article::toLesson)
            .forEach(LingqApi::postLesson)

        else -> lingq.import(articles)
    }

    fun fetchArticles(): List<Article> = MainPage(mainUrl)
        .get()
        .map { ArticlePage(it, ffmepgPath) }
        .map(ArticlePage::get)

    private val logger = this::class.getLogger()

}