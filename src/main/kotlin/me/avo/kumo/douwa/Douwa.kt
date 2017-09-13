package me.avo.kumo.douwa

import me.avo.kumo.util.getFirstByTag
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Douwa {

    val rootUrl = "http://hukumusume.com/douwa"
    val url = "$rootUrl/betu/jap/05/01.htm"

    fun load() = Jsoup.connect(url).get()
            .body()
            .findIndex()
            .let {
                val p = it.getFirstByTag("p")
                val head = p.text()

                val src = p.getFirstByTag("img").attr("src").removePrefix("../../../")
                val image = "$rootUrl/$src"

                it.getFirstByTag("table")

                Hanashi(head, image)
            }


    fun Element.findIndex() = getElementsByTag("td").first { it.attr("width") == "619" }!!

}