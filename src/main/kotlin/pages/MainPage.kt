package pages

import Application
import currentDate
import getText
import getUrl
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class MainPage : Page<List<Pair<String, String>>> {

    //val logger = LogManager.getLogManager().getLogger(Main::class.java.simpleName)

    override val name = "Main_$currentDate.html"
    override val url = Application.mainUrl

    override fun get(): List<Pair<String, String>> {
        val text = load()
        val body = Jsoup.parse(text).body()

        val top = getTopNews(body)
        val list = getNewsList(body)

        return list + top
    }

    fun getTopNews(body: Element): Pair<String, String> {
        val topNews = body.getElementById("topnews")
        val heading = topNews
                .getElementsByTag("h2").first()
                .getElementsByTag("a").first()

        val url = heading.getUrl()
        val title = heading.getText()
        return title to url
    }

    fun getNewsList(body: Element): List<Pair<String, String>> {
        val newsList = body.getElementById("topnewslist")
                .getElementsByTag("ul").first()
                .getElementsByTag("li")

        return newsList.map {
            val link = it
                    .getElementsByTag("h3").first()
                    .getElementsByTag("a").first()
            val url = link.getUrl()
            val title = link.getText()
            title to url
        }
    }


}