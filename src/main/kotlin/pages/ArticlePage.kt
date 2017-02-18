package pages

import data.Article
import getText
import org.jsoup.Jsoup
import java.util.stream.Stream


class ArticlePage(override val url: String) : Page<Article> {

    val id = url.substringAfterLast("/").substringBefore(".html")

    override val name = "Article-$id"

    override fun get(): Article {

        val text = load()

        val body = Jsoup.parse(text).body()

        val title = body.getElementById("newstitle")
                .getElementsByTag("h2").first()
                .getText()

        // TODO get image (id mainimg)
        val img = body.getElementById("mainimg")


        // TODO get audio (id sound)
        val sound = body.getElementById("sound")

        val content = body.getElementById("newsarticle").getText()

        return Article(id = id, content = content, title = title)
    }

    companion object {
        fun getArticles(links: Collection<String>): Stream<Article> = links.parallelStream().map {
            ArticlePage(it).get()
        }
    }


}