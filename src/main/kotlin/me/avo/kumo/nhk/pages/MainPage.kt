package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.NhkException
import me.avo.kumo.nhk.data.Headline
import me.avo.kumo.util.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class MainPage(override val url: String) : Page<List<Headline>> {

    override val name = "Main_$currentDate.html"

    override fun get(): List<Headline> = load()
        .let(Jsoup::parse)
        .body()
        .let(this::getNewsList)

    fun getNewsList(body: Element): List<Headline> = body
        .getElementsByClass("news-list-item")
        .map { it.extractHeadline() }

    private fun Element.extractHeadline(): Headline = try {
        val heading = getFirstByTag("h1")
        val url = heading.getFirstByTag("a").getUrl()
        val title = heading.getTitle()
        val date = getDate()
        val id = url.getIdFromUrl()
        if (title.isBlank()) throw NhkException(id, "The title is blank")
        Headline(id, title, date, url)
    } catch (ex: NullPointerException) {
        throw Exception(
            "There was a problem with the MainPage $name. Likely because the page didn't load correctly. Try reloading.",
            ex
        )
    }

    private fun Element.getTitle() = html().getText()

    private fun Element.getUrl() = makeUrl(this.attr("href"))

    private fun Element.getDate() = makeDate(getFirstByClass("time").text())

    private fun makeUrl(part: String) = url.removeSuffix("/") + part.removePrefix(".")

    private fun String.getIdFromUrl() = substringAfterLast("/").substringBefore(".html")

    override val logger = this::class.getLogger()

}