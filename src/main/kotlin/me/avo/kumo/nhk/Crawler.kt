package me.avo.kumo.nhk

import me.avo.kumo.lingq.Lingq
import me.avo.kumo.lingq.LingqApi
import me.avo.kumo.nhk.pages.ArticlePage
import me.avo.kumo.nhk.pages.MainPage
import me.avo.kumo.util.getLogger

class Crawler(collection: String, val useApi: Boolean) {

    val lingq = Lingq(collection)

    companion object {
        const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    }

    fun fetchAndImport() {
        val articles = fetchArticles() // crawl nhk for articles
        val filtered = NhkMongo.filterImported(articles) // filter for already imported
        logger.info("Found ${filtered.size} articles that have not been imported yet")
        if (filtered.isNotEmpty()) import(filtered)
    }

    fun import(articles: List<Article>) {
        FileArchive.archive(articles) // save files
        if (useApi) articles
                .map(Article::toLesson)
                .forEach { LingqApi.postLesson(it) }
        else lingq.import(articles)
    }

    fun fetchArticles(): List<Article> {
        val headlines = MainPage().get()
        return ArticlePage.getArticles(headlines)
    }

    fun reload() {
        val headlines = getFilteredHeadlines()
        val articles = ArticlePage.getArticles(headlines)
        if (articles.isNotEmpty()) {
            NhkMongo.saveArticles(articles)
            lingq.import(articles)
        }
    }

    fun getFilteredHeadlines(): List<Headline> {
        val headlines = FileArchive.read()
        return NhkMongo.filterHeadlines(headlines)
    }

    private val logger = this::class.getLogger()

}