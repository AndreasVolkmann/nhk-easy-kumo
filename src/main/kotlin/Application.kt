import pages.ArticlePage
import pages.MainPage
import java.io.File


object Application {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"
    val fileDir = PropertyReader.getProperty("dir")

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val news = MainPage().get()

        val articles = ArticlePage.getArticles(news.toMap().values)

        articles.forEach {
            println("${it.id} - ${it.title}")

            val imageFile = File(it.folder.absolutePath + "/image.jpg")
            imageFile.writeBytes(it.image)

            val audioFile = File(it.folder.absolutePath + "/audio.mp3")
            audioFile.writeBytes(it.audio)

            val contentFile = File(it.folder.absolutePath + "/content.txt")
            val finalContent = it.title + "\n" +
                    it.url + "\n" +
                    it.audioUrl + "\n" +
                    it.content
            contentFile.writeText(finalContent)
        }

    }

}