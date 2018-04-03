package me.avo.kumo.pages

import me.avo.kumo.nhk.pages.MainPage
import me.avo.kumo.util.loadResource
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotBeBlank
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

internal class MainPageTest {

    @Test fun `new layout main page should work`() = MainPage("")
        .let { page ->
            val html = this::class.loadResource("new-layout-main.html")
            val body = Jsoup.parse(html).body()
            //val top = page.getTopNews(body)
            val list = page.getNewsList(body)
            list
        }
        .also {
            it.size shouldEqualTo 5
        }
        .forEach {
            println(it)
            it.url.shouldNotBeBlank()
            it.id.shouldNotBeBlank()
            it.title.shouldNotBeBlank()
            it.date.shouldNotBeBlank()
        }

}