package pages

import Crawler
import data.Headline
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import util.*

class MainPage : Page<List<Headline>> {

    override val name = "Main_$currentDate.html"
    override val url = Crawler.mainUrl

    override fun get(): List<Headline> {
        val text = load()
        val body = Jsoup.parse(text).body()

        val top = getTopNews(body)
        val list = getNewsList(body)
        return list + top
    }

    fun getTopNews(body: Element): Headline = body.getElementById("topnews").extractHeadline(true)

    fun getNewsList(body: Element): List<Headline> = body.getElementById("topnewslist")
            .getElementsByTag("ul").first()
            .getElementsByTag("li") // all list items
            .map { it.extractHeadline(false) }


    fun Element.extractHeadline(isTop: Boolean): Headline = try {
        val headingStyle = if (isTop) "h2" else "h3"
        val link = this
                .getElementsByTag(headingStyle).first()
                .getElementsByTag("a").first()
        val url = link.getUrl()
        val title = link.getTitle()
        val date = this.getDate()
        val id = getIdFromUrl(url)
        if (title.isBlank()) throw NhkException(id, "The title is blank")
        Headline(id, title, date, url)
    } catch (ex: NullPointerException) {
        throw Exception("There was a problem with the MainPage $name. Likely because the page didn't load correctly. Try reloading.", ex)
    }


    override val logger = this::class.getLogger()

}