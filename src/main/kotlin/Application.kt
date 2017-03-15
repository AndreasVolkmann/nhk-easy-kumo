import com.beust.jcommander.Parameter
import lingq.Lingq
import org.apache.logging.log4j.LogManager
import storage.Mongo


object Application {

    private val logger = LogManager.getLogger(Application::class.java)

    @Parameter(names = arrayOf("-f --fetch"))
    var fetch = false
        private set


    @JvmStatic
    fun main(args: Array<String>) = try {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val articles = Crawler.fetchArticles() // crawl nhk for articles
        val filtered = Mongo.filterImported(articles) // filter for already imported
        println("Found ${filtered.size} articles that have not been imported yet")

        if (filtered.isNotEmpty()) {
            FileArchive.archive(filtered) // save files
            Lingq.import(filtered) // import to LingQ
        } else Unit
    } catch (ex: Exception) {
        logger.error(ex)
        throw ex
    }


}