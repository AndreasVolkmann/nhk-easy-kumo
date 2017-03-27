import com.beust.jcommander.Parameter
import lingq.Lingq
import org.apache.logging.log4j.LogManager
import storage.Mongo
import storage.Process
import java.awt.Toolkit


object Application {

    private val logger = LogManager.getLogger(Application::class.java)

    @Parameter(names = arrayOf("--mongo", "-m"))
    var mongoPath = "D:\\data\\db"
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val process = if (Process.isRunning()) null else Process.start()
        try {
            Thread.sleep(2000)
            if (Process.isRunning().not()) logger.warn("Process is not running!")
            fetchAndImport()
        } catch (ex: Exception) {
            logger.error(ex)
            Toolkit.getDefaultToolkit().beep()
            throw ex
        } finally {
            if (process != null) Process.stop(process)
        }
    }


    fun fetchAndImport() {
        val articles = Crawler.fetchArticles() // crawl nhk for articles
        val filtered = Mongo.filterImported(articles) // filter for already imported
        println("Found ${filtered.size} articles that have not been imported yet")
        if (filtered.isNotEmpty()) {
            FileArchive.archive(filtered) // save files
            Lingq.import(filtered) // import to LingQ
        } else Unit
    }


}