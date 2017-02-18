import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File


object Application {

    const val mainUrl = "http://www3.nhk.or.jp/news/easy/"

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe")


        val text = File("files/2017-02-18.html").readText()

        val body = Jsoup.parse(text).body()


        val top = getTopNews(body)
        val list = getNewsList(body)

        val news = list + top
        news.forEach {
            println("Title: ${it.first} - Url ${it.second}")
        }


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

    fun Element.getText() = this.html()
            .replace(Regex("(?s)(<rt>.*?)(?:(?:\r*\n){2}|</rt>)"), "")
            .replace("<ruby>", "")
            .replace("</ruby>", "")
            .replace(Regex("( )+"), "")

    fun Element.getUrl() = makeUrl(this.attr("href"))

    fun makeUrl(part: String) = mainUrl.removeSuffix("/") + part.removePrefix(".")


}