package me.avo.kumo.lingq

import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.data.ArticleException
import me.avo.kumo.nhk.persistence.NhkDatabase
import me.avo.kumo.util.Property
import me.avo.kumo.util.getDuration
import me.avo.kumo.util.getLogger
import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.RetryPolicy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

class Lingq(val collection: String, private val database: NhkDatabase) {

    private val url = "https://www.lingq.com/learn/ja/import/contents/?add"
    private val user = Property["lingq.user"]
    private val pass = Property["lingq.pass"]
    private val options = ChromeOptions()
        .addArguments("headless")
        .addArguments("window-size=1600x1200")
    private val logger = this::class.getLogger()

    private val retryPolicy: RetryPolicy = RetryPolicy()
        .retryOn(Exception::class.java)
        .withDelay(1, TimeUnit.SECONDS)
        .withMaxRetries(5)

    fun import(articles: List<Article>) {
        val driver = ChromeDriver(options)
        try {
            articles.forEach { importArticle(driver, it) }
        } finally {
            driver.quit()
        }
    }

    private fun importArticle(driver: ChromeDriver, article: Article) = try {
        logger.info("Importing article ${article.id} - ${article.title}")
        driver.import(article)
        database.updateArticle(article)
    } catch (ex: Exception) {
        throw ArticleException(article, ex)
    }

    private fun ChromeDriver.import(article: Article) = try {
        //manage().window().maximize()
        get(url)
        login()
        sleep(2500)
        addContent(article)
        addAudio(article)
        addTags(article)
        addLevel()
        addCourse()
        addImage(article)
        sleep(1000)
        save()
        sleep(2000)
        share()
        save()
    } catch (ex: Exception) {
        logger.error("Article ${article.id} caused an error")
        throw ex
    }

    private fun ChromeDriver.login() = try {
        val userNameInput = findElementById("id_username")
        userNameInput.sendKeys(user)
        findElementById("id_password").sendKeys(pass)
        userNameInput.submit()
    } catch (ex: org.openqa.selenium.NoSuchElementException) {
        logger.debug("Could not find Login form, assuming already logged in ...")
    }

    private fun ChromeDriver.addContent(article: Article) {
        findElementById("id_title").sendKeys(article.title)
        val finalContent = article.content.first() + article.content
        val contentElement = findElementById("id_text_ifr")
        contentElement.sendKeys(finalContent)
        findElementById("id_original_url").sendKeys(article.url)
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

    private fun ChromeDriver.addAudio(article: Article) = when {
        article.audioUrl != null -> {
            findElementByClassName("lesson-audio-url-btn").click()
            sleep(1000)
            findElementById("id_external_audio").sendKeys(article.audioUrl)
            findElementById("id_duration").sendKeys(article.audioFile.getDuration().toString())
        }
        else -> findElementByName("audio").sendKeys(article.audioFile.absolutePath)
    }

    private fun ChromeDriver.addTags(article: Article) {
        val tags = (listOf("News", "NHK") + article.tags).map { "$it," }.toTypedArray()
        findElementByClassName("select2-search__field").sendKeys(*tags)
    }

    private fun ChromeDriver.share() = retry {
        val shared = findElementByClassName("field-shared")
        if (shared.text != "Shared") {
            findElementById("id_share_status").click() // set status to shared
            sleep(1000)
            println(shared.text)
            shared.click()
        }
    }

    private fun ChromeDriver.addLevel() {
        findElementByCssSelector("#id_level").click()
        sleep(1500)
        findElementByClassName("field-4").click()
    }

    private fun ChromeDriver.addCourse() {
        findElementById("id_collection").click()
        sleep(1000)
        findElementByClassName("field-$collection").click()
    }

    private fun ChromeDriver.save() = retry {
        findElementByClassName("save-button").click()
    }

    private fun retry(block: () -> Unit) = Failsafe.with<Unit>(retryPolicy).run { _ -> block() }

}