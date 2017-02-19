package pages

import Application
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File


interface Page<out T> {

    val name: String

    val url: String

    val path get() = "${Application.fileDir}/articles/"

    val dir get() = File(path)
    val file get() = File("$path/$name")

    fun writeFile(name: String, text: String) = file.writeText(text)

    fun get(): T

    fun load(): String {
        if (dir.exists().not()) dir.mkdirs()
        if (file.exists().not()) {
            println("Today's file $name has not been archived yet, fetching from web ...")
            val driver = ChromeDriver()
            try {
                driver.get(url)
                Thread.sleep(5000)
                val doc = driver.pageSource
                writeFile(name, doc)
            } finally {
                driver.close()
            }
        } else println("Today's file $name has already been archived, reading from file ...")
        return file.readText()
    }

}