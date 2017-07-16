import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import storage.ProcessHandler
import storage.stop
import util.ErrorHandler
import util.getLogger

object Application {

    @Parameter(names = arrayOf("--api", "-a"))
    var useApi = false
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        JCommander.newBuilder().addObject(this).build() // parse cli
        System.setProperty("webdriver.chrome.driver", driverName)
        val process = if (ProcessHandler.isRunning()) null else ProcessHandler.start()
        start(process)
    }


    fun start(process: Process?) = try {
        Thread.sleep(2000)
        if (ProcessHandler.isRunning().not()) logger.warn("Process is not running!")
        Crawler.fetchAndImport()
    } catch (ex: Exception) {
        logger.error(ex)
        ErrorHandler.handle(ex)
    } finally {
        process.stop()
    }

    private val logger = this::class.getLogger()

}