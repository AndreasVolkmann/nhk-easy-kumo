package me.avo.kumo.pages

import me.avo.kumo.nhk.pages.MainPage
import me.avo.kumo.util.loadResource
import org.amshove.kluent.*
import org.jsoup.Jsoup
import org.junit.jupiter.api.Tag
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
            //it.date.
        }

    @Test fun `news item should have heading`() = MainPage("").let { page ->
        val html = this::class.loadResource("Main_Test.html")
        val body = Jsoup.parse(html)
        val headlines = page.getNewsList(body)
        headlines.shouldNotBeEmpty()
        println(headlines.first())
    }

    @Tag("head") @Test fun `load page headless`() {
        val mainPage = MainPage("http://www3.nhk.or.jp/news/easy/")
        val file = mainPage.file
        if (file.exists()) {
            file.delete()
        }
        file.shouldNotExist()
        mainPage.load()
        file.shouldExist()
    }

}