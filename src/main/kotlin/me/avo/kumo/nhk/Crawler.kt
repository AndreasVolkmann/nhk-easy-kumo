package me.avo.kumo.nhk

import me.avo.kumo.lingq.*
import me.avo.kumo.nhk.pages.*
import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.util.*

class Crawler(collection: String, val useApi: Boolean, val database: NhkDatabase) {

    val lingq = Lingq(collection, database)

    companion object {
        const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    }

    fun fetchAndImport() = fetchArticles()
            .let(database::filterImported)
            .also { logger.info("Found ${it.size} articles that have not been imported yet") }
            .let(this::import)

    fun import(articles: List<Article>) {
        if (articles.isEmpty()) return
        FileArchive(database).archive(articles) // save files
        if (useApi) articles
                .map(Article::toLesson)
                .map(LingqApi::postLesson)
        else lingq.import(articles)
    }

    fun fetchArticles(): List<Article> = MainPage().get().let(ArticlePage.Companion::getArticles)

    private val logger = this::class.getLogger()

}