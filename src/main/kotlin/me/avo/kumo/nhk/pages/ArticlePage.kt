package me.avo.kumo.nhk.pages

import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.runBlocking
import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.data.Headline
import me.avo.kumo.nhk.NhkException
import me.avo.kumo.util.getContent
import me.avo.kumo.util.getFirstByTag
import me.avo.kumo.util.getLogger
import me.avo.kumo.util.read
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL

class ArticlePage(val headline: Headline) : Page<Article> {

    override val logger = this::class.getLogger()

    override val url = headline.url

    override val path = "${super.path}/${headline.date}/${headline.id}" // ../articles/2017-02-18/k19439393

    override val name = "Article_${headline.id}.html"

    override fun get(): Article {
        if (dir.exists().not()) dir.mkdirs()

        val text = load()
        val body = Jsoup.parse(text).body()

        val content = getContent(body)
        val (imageUrl, image) = getImage(body)
        val (audioUrl, audio) = getAudio()

        return Article(id = headline.id, url = url, date = headline.date, content = content, title = headline.title,
                image = image, imageUrl = imageUrl, audio = audio, audioUrl = audioUrl, dir = dir, tags = listOf())
    }

    fun getImage(body: Element): Pair<String, ByteArray> = if (Article.getImageFile(dir).exists()) "" to ByteArray(0)
    else {
        val imgUrl = body
                .getElementById("mainimg")
                .getFirstByTag("img")
                .attr("src")

        val finalImageUrl = if (imgUrl.startsWith("http")) imgUrl else url.removeSuffix("html") + "jpg"
        val imageBytes = URL(finalImageUrl).read()
        finalImageUrl to imageBytes
    }

    fun getAudio(): Pair<String, ByteArray> {
        val audioUrl = url.removeSuffix("html") + "mp3"
        return if (Article.getAudioFile(dir).exists()) audioUrl to ByteArray(0)
        else audioUrl to URL(audioUrl).read()
    }

    fun getContent(body: Element): String {
        val content = body.getElementById("newsarticle").getContent()
        if (content.contains("<") and content.contains(">"))
            throw NhkException(headline.id, "Content contains illegal characters: $content")
        else return content
    }

    companion object {

        fun getArticles(links: Collection<Headline>): List<Article> = runBlocking {
            links
                    .map(::ArticlePage)
                    .map { future { it.get() } }
                    .map { it.join() }
        }

    }


}