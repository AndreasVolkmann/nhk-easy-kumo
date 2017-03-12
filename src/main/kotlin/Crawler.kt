import data.Article
import pages.ArticlePage
import pages.MainPage

object Crawler {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"

    fun fetchArticles(): List<Article> {
        val headlines = MainPage().get()
        return ArticlePage.getArticles(headlines)
    }


}