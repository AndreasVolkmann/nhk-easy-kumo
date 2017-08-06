import storage.ProcessHandler
import storage.stop
import util.ErrorHandler
import util.getLogger

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Args.parse(args)
        System.setProperty("webdriver.chrome.driver", driverName)
        val process = if (ProcessHandler.isRunning()) null else ProcessHandler.start()
        start(process)
    }

    fun start(process: Process?) = try {
        Thread.sleep(2000)
        if (ProcessHandler.isRunning().not()) logger.warn("Process is not running!")
        val crawler = Crawler(Args.collection, Args.useApi)
        crawler.fetchAndImport()
    } catch (ex: Exception) {
        logger.error(ex)
        ErrorHandler.handle(ex)
    } finally {
        process.stop()
    }

    private val logger = this::class.getLogger()

}