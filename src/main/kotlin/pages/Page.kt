package pages

import org.openqa.selenium.chrome.ChromeDriver
import java.io.File


interface Page<T> {

    val name: String

    val url: String

    val path get() = "${Application.fileDir}/$name.html"

    val file get() = File(path)

    fun writeFile(text: String) = file.writeText(text)

    fun get(): T

    fun load(): String {
        if (file.exists().not()) {
            println("Today's file $name has not been archived yet, fetching from web ...")
            val driver = ChromeDriver()
            try {
                driver.get(url)
                Thread.sleep(5000)
                val doc = driver.pageSource
                writeFile(doc)
            } finally {
                driver.close()
            }
        } else println("Today's file $name has already been archived, reading from file ...")
        return file.readText()
    }

}