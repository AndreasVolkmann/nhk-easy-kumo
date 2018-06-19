package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.NhkException
import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.data.ArticleException
import me.avo.kumo.nhk.data.Headline
import me.avo.kumo.nhk.processing.audio.AudioParser
import me.avo.kumo.util.dateToString
import me.avo.kumo.util.getLogger
import me.avo.kumo.util.getText
import me.avo.kumo.util.read
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.net.URL

class ArticlePage(val headline: Headline, private val ffmpegPath: String) : Page<Article> {

    override val logger = this::class.getLogger()

    override val url = headline.url

    override val path =
        "${super.path}/${headline.date.dateToString()}/${headline.id}" // ../articles/2017-02-18/k19439393

    override val name = "Article_${headline.id}.html"

    override fun get(): Article = try {
        if (!dir.exists()) dir.mkdirs()

        val text = load()
        val body = Jsoup.parse(text).body()

        val content = getContent(body)
        val (imageUrl, image) = getImage(body) ?: null to null
        val audio = getAudio()
        Article(
            id = headline.id, url = url, date = headline.date, content = content, title = headline.title,
            image = image, imageUrl = imageUrl, audioFile = audio, audioUrl = null, dir = dir, tags = listOf()
        )
    } catch (ex: Exception) {
        throw ArticleException(headline, ex)
    }

    fun getImage(body: Element): Pair<String, ByteArray>? = when {
        Article.getImageFile(dir).exists() -> null
        else -> body
            .getElementById("js-article-figure")
            .getElementsByTag("img")
            .firstOrNull()
            ?.attr("src")
            ?.let { if (it.startsWith("http")) it else url.removeSuffix("html") + "jpg" }
            ?.let { it to URL(it).read() }
    }

    private fun getAudio(): File {
        val audioFile = File(dir, "audio.mp3")
        return when (audioFile.exists()) {
            true -> audioFile
            false -> AudioParser(dir, dir, ffmpegPath).run(headline.id)
        }
    }

    fun getContent(body: Element): String {
        val content = body.getElementsByAttribute("id").mapNotNull {
            it.getElementWithId("newsarticle") ?: it.getElementWithId("js-article-body")
        }.first().getText()

        if (content.contains("<") and content.contains(">"))
            throw NhkException(headline.id, "Content contains illegal characters: $content")
        else return content
    }

    private fun Element.getText() = getElementsByTag("p").html().getText()
    private fun Element.getElementWithId(id: String) = getElementsByAttributeValue("id", id).firstOrNull()

}