import data.Article
import data.Headline
import lingq.Lingq
import pages.ArticlePage
import pages.MainPage
import storage.FileArchive
import storage.NhkMongo
import util.getLogger

object Crawler {

    private val logger = this::class.getLogger()

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"

    fun fetchAndImport() {
        val articles = Crawler.fetchArticles() // crawl nhk for articles
        val filtered = NhkMongo.filterImported(articles) // filter for already imported
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
            NhkMongo.saveArticles(articles)
            Lingq.import(articles)
        }
    }

    fun getFilteredHeadlines(): List<Headline> {
        val headlines = FileArchive.read()
        return NhkMongo.filterHeadlines(headlines)
    }


}