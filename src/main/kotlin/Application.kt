import pages.ArticlePage
import pages.MainPage


object Application {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    val fileDir = PropertyReader.getProperty("dir")

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val headlines = MainPage().get()
        val articles = ArticlePage.getArticles(headlines)

        articles.forEach {
            with(it) {
                println("$date - $id - $title")
                imageFile.writeIfNotExists(image)
                audioFile.writeIfNotExists(audio)
                contentFile.writeIfNotExists(finalContent)
            }
        }

    }


}