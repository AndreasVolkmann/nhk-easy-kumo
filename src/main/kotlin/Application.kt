import com.beust.jcommander.Parameter
import org.apache.logging.log4j.LogManager
import storage.ProcessHandler
import storage.stop
import java.awt.Toolkit


object Application {

    private val logger = LogManager.getLogger(Application::class.java)

    @Parameter(names = arrayOf("--mongo", "-m"))
    var mongoPath = "D:\\data\\db"
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")

        val process = if (ProcessHandler.isRunning()) null else ProcessHandler.start()
        try {
            Thread.sleep(2000)
            if (ProcessHandler.isRunning().not()) logger.warn("Process is not running!")
            Crawler.fetchAndImport()
        } catch (ex: Exception) {
            logger.error(ex)
            Toolkit.getDefaultToolkit().beep()
            throw ex
        } finally {
            process.stop()
        }
    }


}