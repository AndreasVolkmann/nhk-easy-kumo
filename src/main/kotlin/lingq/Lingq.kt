package lingq

import PropertyReader.getProperty
import data.Article
import getDuration
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import storage.Mongo
import java.lang.Thread.sleep

/**
 * Created by Av on 3/9/2017.
 */
object Lingq {

    const val url = "https://www.lingq.com/learn/ja/import/contents/?add"
    val user = getProperty("lingq.user")
    val pass = getProperty("lingq.pass")
    val options = ChromeOptions()

    fun import(articles: List<Article>) {
        val driver = ChromeDriver(options)
        try {
            articles.forEach {
                driver.import(it)
                Mongo.updateArticle(it)
            }
        } finally {
            driver.quit()
        }
    }


    fun ChromeDriver.import(article: Article) {
        // Login Page
        get(url)
        try {
            val userNameInput = findElementById("id_username")
            userNameInput.sendKeys(user)
            findElementById("id_password").sendKeys(pass)
            userNameInput.submit()
        } catch (ex: org.openqa.selenium.NoSuchElementException) {
            println("Could not find Login form, assuming already logged in ...")
        }

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
        sleep(1000)
        findElementByClassName("field-4").click()

        // Course
        findElementById("id_collection").click()
        sleep(100)
        findElementByClassName("field-266730").click()

        val imagePath = article.imageFile.absolutePath
        findElementByClassName("lesson-image").click()
        sleep(500)
        val pictures = findElementsByClassName("picture")
        pictures[1].sendKeys(imagePath)
        sleep(5000)
        findElementByClassName("finish").click()
        sleep(500)
        save()

        findElementById("id_share_status").click() // set status to shared
        sleep(600)
        findElementByClassName("field-shared").click()
        save()
    }

    fun ChromeDriver.save() {
        findElementByClassName("save-button").click()
        sleep(3000)
    }


}