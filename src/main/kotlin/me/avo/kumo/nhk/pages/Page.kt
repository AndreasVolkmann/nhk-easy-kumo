package me.avo.kumo.nhk.pages

import org.apache.logging.log4j.Logger
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File


interface Page<out T> {

    val logger: Logger

    val name: String

    val url: String

    val path get() = "articles/"

    val dir get() = File(path)
    val file get() = File("$path/$name")

    fun writeFile(name: String, text: String) = file.writeText(text)

    fun get(): T

    fun load(): String {
        if (dir.exists().not()) dir.mkdirs()
        if (file.exists().not()) {
            logger.info("Today's file $name has not been archived yet, fetching from web ...")
            val driver = ChromeDriver()
            try {
                driver.get(url)
                Thread.sleep(3000)
                val doc = driver.pageSource
                writeFile(name, doc)
            } finally {
                driver.close()
            }
        } else logger.info("Today's file $name has already been archived, reading from file ...")
        return file.readText()
    }

}