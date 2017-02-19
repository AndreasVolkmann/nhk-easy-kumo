package pages

import data.Article
import data.Headline
import getText
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.BufferedInputStream
import java.net.URL
import java.util.stream.Stream


class ArticlePage(val headline: Headline) : Page<Article> {

    override val url = headline.url

    override val path = "${super.path}/${headline.date}/${headline.id}" // ../articles/2017-02-18/k19439393

    override val name = "Article_${headline.id}.html"

    override fun get(): Article {
        if (dir.exists().not()) dir.mkdirs()

        val text = load()
        val body = Jsoup.parse(text).body()

        val content = getContent(body)
        val image = getImage(body)
        val (audioUrl, audio) = getAudio()

        return Article(id = headline.id, url = url, date = headline.date, content = content, title = headline.title,
                image = image, audio = audio, audioUrl = audioUrl, dir = dir)
    }

    fun getImage(body: Element): ByteArray = if (Article.getImageFile(dir).exists()) ByteArray(0)
    else {
        val imgUrl = body.getElementById("mainimg")
                .getElementsByTag("img").first()
                .attr("src")

        val finalImageUrl = if (imgUrl.startsWith("http")) imgUrl else url.removeSuffix("html") + "jpg"

        BufferedInputStream(URL(finalImageUrl).openStream()).use {
            it.readBytes(44)
        }
    }


    fun getAudio(): Pair<String, ByteArray> {
        val audioUrl = url.removeSuffix("html") + "mp3"
        return if (Article.getAudioFile(dir).exists()) audioUrl to ByteArray(0)
        else {
            val audio = BufferedInputStream(URL(audioUrl).openStream()).use {
                it.readBytes()
            }
            audioUrl to audio
        }
    }

    fun getContent(body: Element) = body.getElementById("newsarticle").getText()

    companion object {
        fun getArticles(links: Collection<Headline>): Stream<Article> = links.parallelStream().map {
            ArticlePage(it).get()
        }
    }


}