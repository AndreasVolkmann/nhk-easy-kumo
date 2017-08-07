package jlpt

import data.Lesson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pages.Page
import util.getLogger
import util.loadResource
import java.net.URLDecoder

class Jt4y(val level: Level, val fromFile: Boolean = false) : Page<List<Lesson>> {

    override val name = "JLPT.html"

    override val url = "http://japanesetest4you.com/jlpt-$level-grammar-list/"

    val local = this::class.loadResource("JLPT${level}GrammarList–Japanesetest4you.html")

    override fun get(): List<Lesson> = getDocument()
            .getElementsByClass("entry").first()
            .getElementsByTag("a")
            .dropLast(1)
            .map { it.attr("href") }
            .map {
                val keyword = if (it.contains("grammar-%")) "grammar-" else "grammar-n2-"
                val mid = it.substringAfter(keyword).substringBefore("-")
                val start = it.substringBefore(mid)
                val end = it.substringAfter(mid)
                start + URLDecoder.decode(mid, "UTF-8") + end
            }
            .onEach { println(it) }
            .map { Resource.Web(it) }
            .map { Jt4yLesson(it).extract() }

    fun getDocument(): Document = if (fromFile) Jsoup.parse(local)
    else Jsoup.connect(url).get()

    override val logger = this::class.getLogger()

}