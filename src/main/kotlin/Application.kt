import pages.ArticlePage
import pages.MainPage


object Application {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    const val fileDir = "files"

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")


        val news = MainPage().get()
        news.forEach {
            println("Title: ${it.first} - Url ${it.second}")
        }

        val articles = ArticlePage.getArticles(news.toMap().values)

        articles.forEach {
            println("${it.title} - ${it.content}")
        }


    }


}