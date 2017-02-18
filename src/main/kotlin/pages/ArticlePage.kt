package pages

import currentDate
import data.Article
import getText
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
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

        val title = getTitle(body)
        val content = getContent(body)
        val image = getImage(body)
        val (audioUrl, audio) = getAudio(body)

        return Article(id = id, url = url, date = currentDate, content = content, title = title,
                image = image, audio = audio, audioUrl = audioUrl)
    }

    fun getTitle(body: Element) = body.getElementById("newstitle")
            .getElementsByTag("h2").first()
            .getText()

    fun getImage(body: Element): ByteArray {
        val imgUrl = body.getElementById("mainimg")
                .getElementsByTag("img").first()
                .attr("src")

        val finalImageUrl = if (imgUrl.startsWith("http")) imgUrl else url.removeSuffix("html") + "jpg"

        return BufferedInputStream(URL(finalImageUrl).openStream()).use {
            it.readBytes(44)
        }
    }

    fun getAudio(body: Element): Pair<String, ByteArray> {
        val audioUrl = url.removeSuffix("html") + "mp3"
        val audio = BufferedInputStream(URL(audioUrl).openStream()).use {
            it.readBytes()
        }
        return audioUrl to audio
    }

    fun getContent(body: Element) = body.getElementById("newsarticle").getText()

    companion object {
        fun getArticles(links: Collection<String>): Stream<Article> = links.parallelStream().map {
            ArticlePage(it).get()
        }
    }


}