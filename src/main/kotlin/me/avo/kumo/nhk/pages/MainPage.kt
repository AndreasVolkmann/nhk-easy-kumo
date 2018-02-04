package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.*
import me.avo.kumo.nhk.data.*
import me.avo.kumo.util.*
import org.jsoup.*
import org.jsoup.nodes.*

class MainPage(override val url: String) : Page<List<Headline>> {

    override val name = "Main_$currentDate.html"

    override fun get(): List<Headline> = load()
        .let(Jsoup::parse)
        .body()
        .let(this::getNews)

    fun getNews(body: Element): List<Headline> = getNewsList(body) + getTopNews(body)

    fun getTopNews(body: Element): Headline = body.getElementById("topnews").extractHeadline(true)

    fun getNewsList(body: Element): List<Headline> = body.getElementById("topnewslist")
        .getFirstByTag("ul")
        .getElementsByTag("li") // all list items
        .map { it.extractHeadline(false) }

    fun Element.extractHeadline(isTop: Boolean): Headline = try {
        val headingStyle = if (isTop) "h2" else "h3"
        val link = this
            .getFirstByTag(headingStyle)
            .getFirstByTag("a")
        val url = link.getUrl()
        val title = link.getTitle()
        val date = getDate()
        val id = getIdFromUrl(url)
        if (title.isBlank()) throw NhkException(id, "The title is blank")
        Headline(id, title, date, link.getUrl())
    } catch (ex: NullPointerException) {
        throw Exception(
            "There was a problem with the MainPage $name. Likely because the page didn't load correctly. Try reloading.",
            ex
        )
    }

    private fun Element.getUrl() = makeUrl(this.attr("href"))

    private fun makeUrl(part: String) = url.removeSuffix("/") + part.removePrefix(".")

    override val logger = this::class.getLogger()

}