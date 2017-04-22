import data.Article
import lingq.Lingq
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File


class LingqTest {

    companion object {
        val path = "V:\\NHK-Easy\\articles\\2017-03-10\\k10010903831000"
        val content = "This is just a test!"
        val article = Article("1", "test.com", "Test Article", "2017-03-11", content, ByteArray(0), ByteArray(0), "audio.com", File(path))
    }

    @Test
    fun connect() {
        Lingq.import(listOf(article))
    }

    @Test
    fun importSingle() {
        article.content shouldEqualTo content
        val content = article.content.first() + article.content
        val driver = ChromeDriver(Lingq.options)
        try {
            with(driver) {
                // Login Page
                get(Lingq.url)
                try {
                    val userNameInput = findElementById("id_username")
                    userNameInput.sendKeys(Lingq.user)
                    findElementById("id_password").sendKeys(Lingq.pass)
                    userNameInput.submit()
                } catch (ex: org.openqa.selenium.NoSuchElementException) {
                    Lingq.logger.debug("Could not find Login form, assuming already logged in ...")
                }


                // Import Page
                Thread.sleep(2500)
                findElementById("id_title").sendKeys(article.title)
                Thread.sleep(500)
                findElementById("id_text_ifr").sendKeys(content)
                //findElementById("id_text_ifr").text shouldEqualTo article.content
                try {
                    val element = findElementById("tinymce")
                    val text = element.text
                    println(text)
                } catch (ex: Exception) {
                    println(ex)
                    println("Couldn't find the element")
                }
                Thread.sleep(500)
                findElementById("id_original_url").sendKeys(article.url)
                Thread.sleep(5000)
            }
        } finally {
            driver.quit()
        }
    }

}