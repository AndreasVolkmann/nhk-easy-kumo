import data.Article
import data.Headline
import lingq.Lingq
import pages.ArticlePage
import pages.MainPage
import storage.Mongo

object Crawler {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"

    fun fetchArticles(): List<Article> {
        val headlines = MainPage().get()
        return ArticlePage.getArticles(headlines)
    }

    fun reload() {
        val headlines = getFilteredHeadlines()
        val articles = ArticlePage.getArticles(headlines)
        if (articles.isNotEmpty()) {
            Mongo.saveArticles(articles)
            Lingq.import(articles)
        }
    }

    fun getFilteredHeadlines(): List<Headline> {
        val headlines = FileArchive.read()
        return Mongo.filterHeadlines(headlines)
    }


}