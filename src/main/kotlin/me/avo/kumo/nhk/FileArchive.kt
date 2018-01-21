package me.avo.kumo.nhk

import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.util.*
import java.io.*

class FileArchive(private val database: NhkDatabase) {

    fun archive(articles: List<Article>) {
        articles.forEach(Article::makeFiles)
        database.saveArticles(articles)
    }

    fun read(): List<Headline> = getFolders().flatMap {
        val date = it.name
        it.listFiles().map {
            val path = it.absolutePath
            val file = File("$path/content.txt")
            val content = file.readLines()
            Headline(id = it.name, title = content[0], url = content[1], date = date)
        }
    }

    fun getFolders(): Array<out File> = File("articles").listFiles()

}

private fun Article.makeFiles() {
    println("$date - $id - $title")
    imageFile.writeIfNotExists(image)
    audioFile.writeIfNotExists(audio)
}
