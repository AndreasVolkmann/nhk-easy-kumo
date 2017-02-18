import org.openqa.selenium.chrome.ChromeDriver
import java.util.logging.LogManager


class Main : Page {

    val logger = LogManager.getLogManager().getLogger(Main::class.java.simpleName)

    override val url = Application.mainUrl

    override fun get(): String {
        val driver = ChromeDriver()
        driver.get(url)
        Thread.sleep(5000)
        val doc = driver.pageSource
        val path = writeFile(doc)
        driver.close()
        return path
    }


}