package lingq

import PropertyReader
import data.Article
import getDuration
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.lang.Thread.sleep

/**
 * Created by Av on 3/9/2017.
 */
class Lingq(val article: Article) {

    companion object {
        const val url = "https://www.lingq.com/learn/ja/import/contents/?add"
        val user = PropertyReader.getProperty("lingq.user")
        val pass = PropertyReader.getProperty("lingq.pass")
    }


    fun import() {
        val options = ChromeOptions()
        val driver = ChromeDriver(options)
        try {
            with(driver) {
                // Login Page
                get(url)
                val userNameInput = findElementById("id_username")
                userNameInput.sendKeys(user)
                findElementById("id_password").sendKeys(pass)
                userNameInput.submit()

                // Import Page
                sleep(2000)
                findElementById("id_title").sendKeys(article.title)
                findElementById("id_text_ifr").sendKeys(article.content)
                findElementById("id_original_url").sendKeys(article.url)

                // audio
                findElementByClassName("lesson-audio-url-btn").click()
                sleep(500)
                findElementById("id_external_audio").sendKeys(article.audioUrl)
                findElementById("id_duration").sendKeys(article.audioFile.getDuration().toString())
                findElementByClassName("select2-search__field").sendKeys("News,", "NHK,")

                // Level
                findElementByCssSelector("#id_level").click()
                sleep(500)
                findElementByClassName("field-4").click()

                // Course
                findElementById("id_collection").click()
                sleep(500)
                findElementByClassName("field-266730").click()

                val imagePath = article.imageFile.absolutePath
                findElementByClassName("lesson-image").click()
                sleep(500)
                val pictures = findElementsByClassName("picture")
                pictures[1].sendKeys(imagePath)
                sleep(5000)
                findElementByClassName("finish").click()

                sleep(5000)
            }
        } finally {
            driver.close()
        }

    }


}