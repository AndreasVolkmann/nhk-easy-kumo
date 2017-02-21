import javazoom.jl.decoder.Bitstream
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

                val length = audioFile.inputStream().use {
                    val stream = Bitstream(it)
                    val h = stream.readFrame()
                    val tn = it.channel.size()
                    val ms = h.ms_per_frame()
                    val bitrate = h.bitrate()
                    val frame = h.calculate_framesize()
                    println("$id Frame: $frame, ms: $ms, bitrate: $bitrate, channel: $tn, ${tn / 10000}" )
                    tn / 10000
                }

                val html = Application::class.java.classLoader.getResource("template.html")
                        .readText()
                        .replace("{title}", title)
                        .replace("{articleUrl}", url)
                        .replace("{audioUrl}", audioUrl)
                        .replace("{audioLength}", length.toString())
                        .replace("{body}", content)
                htmlFile.writeIfNotExists(html)
            }
        }

    }


}