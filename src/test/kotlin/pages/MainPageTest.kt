package pages

import org.amshove.kluent.shouldNotBeBlank
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import util.loadResource

internal class MainPageTest {

    @Test
    fun `Headline should have a title`() = MainPage()
            .let { page ->
                val html = this::class.loadResource("Main_2017-05-17.html")
                val body = Jsoup.parse(html).body()
                val top = page.getTopNews(body)
                val list = page.getNewsList(body)
                (list + top)
            }
            .forEach {
                println("Article: ${it.id}")
                it.title.shouldNotBeBlank()
            }


}