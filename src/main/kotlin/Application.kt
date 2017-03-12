import data.Article
import pages.ArticlePage
import pages.MainPage
import storage.Mongo


object Application {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    val fileDir = PropertyReader.getProperty("dir")

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")
        val articles = fetchArticles()
        archive(articles)
    }

    fun fetchArticles(): List<Article> {
        val headlines = MainPage().get()
        return ArticlePage.getArticles(headlines)
    }

    fun archive(articles: List<Article>) {
        articles.forEach(Article::makeFiles)
        Mongo.saveArticles(articles.toList())
    }


}