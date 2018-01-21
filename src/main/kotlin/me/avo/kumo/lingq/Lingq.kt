package me.avo.kumo.lingq

import me.avo.kumo.nhk.data.*
import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.util.*
import net.jodah.failsafe.*
import org.openqa.selenium.chrome.*
import java.lang.Thread.sleep
import java.util.concurrent.*

class Lingq(val collection: String, private val database: NhkDatabase) {

    private val url = "https://www.lingq.com/learn/ja/import/contents/?add"
    private val user = Property["lingq.user"]
    private val pass = Property["lingq.pass"]
    private val options = ChromeOptions()
    private val logger = this::class.getLogger()

    private val retryPolicy: RetryPolicy = RetryPolicy()
            .retryOn(Exception::class.java)
            .withDelay(1, TimeUnit.SECONDS)
            .withMaxRetries(5)

    fun import(articles: List<Article>) {
        val driver = ChromeDriver(options)
        try {
            articles.forEach {
                driver.import(it)
                database.updateArticle(it)
            }
        } finally {
            driver.quit()
        }
    }


    private fun ChromeDriver.import(article: Article) = try {
        // Login Page
        manage().window().maximize()
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
        findElementByClassName("field-$collection").click()

        addImage(article)
        sleep(1000)
        save()
        sleep(3000)
        retry {
            val shared = findElementByClassName("field-shared")
            if (shared.text != "Shared") {
                findElementById("id_share_status").click() // set status to shared
                sleep(1000)
                println(shared.text)
                shared.click()
            }
        }
        save()
    } catch (ex: Exception) {
        logger.error("Article ${article.id} caused an error")
        throw ex
    }

    private fun ChromeDriver.addImage(article: Article) = retry {
        val file = article.imageFile
        if (file.exists()) {
            val imagePath = file.absolutePath
            findElementByClassName("lesson-image").click()
            sleep(1000)
            findElementsByClassName("picture")[1].sendKeys(imagePath)
            sleep(5000)
            findElementByClassName("finish").click()
        }
    }

    private fun ChromeDriver.save() = retry {
        findElementByClassName("save-button").click()
    }

    private fun retry(block: () -> Unit) = Failsafe.with<Unit>(retryPolicy).run { _ -> block() }


}