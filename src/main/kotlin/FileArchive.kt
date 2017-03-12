import PropertyReader.getProperty
import data.Article
import storage.Mongo
import java.io.File

object FileArchive {

    val fileDir = getProperty("dir")

    fun archive(articles: List<Article>) {
        articles.forEach(Article::makeFiles)
        Mongo.saveArticles(articles.toList())
    }

    fun read() {

        val dir = File(fileDir)
        dir.listFiles().forEach {
            TODO()
        }

    }

}

fun Article.makeFiles() {
    println("$date - $id - $title")
    imageFile.writeIfNotExists(image)
    audioFile.writeIfNotExists(audio)
    htmlFile.writeIfNotExists(this.toHtml())
}


fun Article.toHtml() = Application::class.java.classLoader.getResource("template.html")
        .readText()
        .replace("{title}", title)
        .replace("{articleUrl}", url)
        .replace("{audioUrl}", audioUrl)
        .replace("{audioLength}", audioFile.getDuration().toString())
        .replace("{body}", content)

