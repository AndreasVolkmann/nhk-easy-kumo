package pages

import currentDate
import data.Article
import getText
import org.jsoup.Jsoup
import java.io.BufferedInputStream
import java.io.File
import java.net.URL
import java.util.stream.Stream


class ArticlePage(override val url: String) : Page<Article> {

    val id = url.substringAfterLast("/").substringBefore(".html")

    val folder = File("${Application.fileDir}/$currentDate/$id")

    override val name = "$id/Article.html"

    override fun get(): Article {
        if (folder.exists().not()) folder.mkdirs()

        val text = load()

        val body = Jsoup.parse(text).body()

        val title = body.getElementById("newstitle")
                .getElementsByTag("h2").first()
                .getText()

        val imgUrl = body.getElementById("mainimg")
                .getElementsByTag("img").first()
                .attr("src")

        val finalImageUrl = if (imgUrl.startsWith("http")) imgUrl else url.removeSuffix("html") + "jpg"

        val image = BufferedInputStream(URL(finalImageUrl).openStream()).use {
            it.readBytes(44)
        }


        // TODO get audio (id sound)
        val audioUrl = url.removeSuffix("html") + "mp3"
        val audio = BufferedInputStream(URL(audioUrl).openStream()).use {
            it.readBytes()
        }


        val content = body.getElementById("newsarticle").getText()

        return Article(id = id, url = url, date = currentDate, content = content, title = title,
                image = image, audio = audio, audioUrl = audioUrl)
    }

    companion object {
        fun getArticles(links: Collection<String>): Stream<Article> = links.parallelStream().map {
            ArticlePage(it).get()
        }
    }


}