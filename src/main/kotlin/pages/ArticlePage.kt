package pages

import data.Article
import data.Headline
import getText
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import read
import java.net.URL
import kotlin.streams.toList


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
        URL(finalImageUrl).read()
    }


    fun getAudio(): Pair<String, ByteArray> {
        val audioUrl = url.removeSuffix("html") + "mp3"
        return if (Article.getAudioFile(dir).exists()) audioUrl to ByteArray(0)
        else audioUrl to URL(audioUrl).read()
    }

    fun getContent(body: Element) = body.getElementById("newsarticle").getText()

    companion object {
        fun getArticles(links: Collection<Headline>) = links.parallelStream().map {
            ArticlePage(it).get()
        }.toList()
    }


}