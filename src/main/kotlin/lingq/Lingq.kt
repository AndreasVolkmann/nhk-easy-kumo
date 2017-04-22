package lingq

import util.PropertyReader.getProperty
import data.Article
import net.jodah.failsafe.Failsafe
import util.getDuration
import net.jodah.failsafe.RetryPolicy
import org.apache.logging.log4j.LogManager
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import storage.NhkMongo
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

/**
 * Created by Av on 3/9/2017.
 */
object Lingq {

    const val url = "https://www.lingq.com/learn/ja/import/contents/?add"
    val user = getProperty("lingq.user")
    val pass = getProperty("lingq.pass")
    val options = ChromeOptions()
    val logger = LogManager.getLogger(Lingq::class.java)!!

    val retryPolicy: RetryPolicy = RetryPolicy()
            .retryOn(Exception::class.java)
            .withDelay(1, TimeUnit.SECONDS)
            .withMaxRetries(5)

    fun import(articles: List<Article>) {
        val driver = ChromeDriver(options)
        try {
            articles.forEach {
                driver.import(it)
                NhkMongo.updateArticle(it)
            }
        } finally {
            driver.quit()
        }
    }


    fun ChromeDriver.import(article: Article) = try {
        // Login Page
        get(url)
        try {
            val userNameInput = findElementById("id_username")
            userNameInput.sendKeys(user)
            findElementById("id_password").sendKeys(pass)
            userNameInput.submit()
        } catch (ex: org.openqa.selenium.NoSuchElementException) {
            logger.debug("Could not find Login form, assuming already logged in ...")
        }


        // Import Page
        sleep(2500)
        findElementById("id_title").sendKeys(article.title)
        val finalContent = article.content.first() + article.content
        val contentElement = findElementById("id_text_ifr")
        contentElement.sendKeys(finalContent)
        findElementById("id_original_url").sendKeys(article.url)

        // audio
        findElementByClassName("lesson-audio-url-btn").click()
        sleep(1000)
        findElementById("id_external_audio").sendKeys(article.audioUrl)
        findElementById("id_duration").sendKeys(article.audioFile.getDuration().toString())
        findElementByClassName("select2-search__field").sendKeys("News,", "NHK,")

        // Level
        findElementByCssSelector("#id_level").click()
        sleep(1500)
        findElementByClassName("field-4").click()

        // Course
        findElementById("id_collection").click()
        sleep(1000)
        findElementByClassName("field-266730").click()

        addImage(article)
        sleep(1000)
        save()

        Failsafe.with<Unit>(retryPolicy).run { _ ->
            findElementById("id_share_status").click() // set status to shared
            sleep(1000)
            val shared = findElementByClassName("field-shared")
            println(shared.text)
            shared.click()
            save()
        }
    } catch (ex: Exception) {
        logger.error("Article ${article.id} caused an error")
        throw ex
    }

    fun ChromeDriver.addImage(article: Article) {
        val imagePath = article.imageFile.absolutePath
        findElementByClassName("lesson-image").click()
        sleep(1000)
        val pictures = findElementsByClassName("picture")
        pictures[1].sendKeys(imagePath)
        sleep(5000)
        findElementByClassName("finish").click()
    }

    fun ChromeDriver.save() = Failsafe.with<Unit>(retryPolicy).run { _ ->
        findElementByClassName("save-button").click()
        sleep(3000)
    }


}