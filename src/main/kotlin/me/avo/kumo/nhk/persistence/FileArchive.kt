package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.data.Headline
import me.avo.kumo.util.writeIfNotExists
import java.io.File

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
    image?.let(imageFile::writeIfNotExists)
    //audioFile.writeIfNotExists(audio)
}
