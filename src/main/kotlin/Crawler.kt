import data.Article
import data.Headline
import lingq.Lingq
import org.apache.logging.log4j.LogManager
import pages.ArticlePage
import pages.MainPage
import storage.FileArchive
import storage.Mongo

object Crawler {

    private val logger = LogManager.getLogger(Crawler::class.java)

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"

    fun fetchAndImport() {
        val articles = Crawler.fetchArticles() // crawl nhk for articles
        val filtered = Mongo.filterImported(articles) // filter for already imported
        logger.info("Found ${filtered.size} articles that have not been imported yet")
        if (filtered.isNotEmpty()) {
            FileArchive.archive(filtered) // save files
            Lingq.import(filtered) // import to LingQ
        } else Unit
    }

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