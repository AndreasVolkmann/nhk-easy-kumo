import com.beust.jcommander.Parameter
import lingq.Lingq
import storage.Mongo


object Application {

    @Parameter(names = arrayOf("-f --fetch"))
    var fetch = false
        private set


    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val articles = Crawler.fetchArticles() // crawl nhk for articles
        val filtered = Mongo.filterImported(articles) // filter for already imported
        println("Found ${filtered.size} articles that have not been imported yet")

        FileArchive.archive(filtered) // save files
        Lingq.import(filtered) // import to LingQ
    }


}