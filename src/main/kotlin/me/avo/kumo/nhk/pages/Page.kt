package me.avo.kumo.nhk.pages

import org.apache.logging.log4j.Logger
import java.io.File

interface Page<out T> {

    val logger: Logger

    val name: String

    val url: String

    val path get() = "articles/"

    val dir get() = File(path)
    val file get() = File(dir, name)

    fun writeFile(text: String) = file.writeText(text)

    fun get(): T

    /**
     * Load the dom content from [url], write to [file] and return it
     */
    fun load(): String {
        if (dir.exists().not()) dir.mkdirs()
        when {
            file.exists().not() -> {
                logger.info("Today's file $name has not been archived yet, fetching from web ...")
                val chromePath = "'C:/Program Files (x86)/Google/Chrome/Application/chrome.exe'"
                val gitPath = "D:\\Programme\\Git\\bin\\bash.exe"
                val quote = "\""
                val filePath = file.absolutePath.replace("\\", "/")
                val command = "$quote$chromePath --headless --disable-gpu --dump-dom $url > $filePath$quote"
                val fullCommand = mutableListOf(gitPath, "-c", command)
                //println(fullCommand.joinToString(" "))

                ProcessBuilder(fullCommand).start().let {
                    it.waitFor()
                    //it.errorStream.use { it.bufferedReader().use { it.readLines().forEach(::println) } }
                    //it.waitFor().also { println("Exited with $it") }
                }
            }
            else -> {
                logger.info("Today's file $name has already been archived, reading from file ...")
            }
        }
        return file.readText()
    }

}