package jlpt

import data.Lesson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import pages.Page
import util.getLogger
import java.net.URLDecoder

/**
 * Created by Av on 4/22/2017.
 */
class Jt4y(val fromFile: Boolean = false) : Page<List<Lesson>> {

    override val logger = this::class.getLogger()

    override val name = "JLPT.html"

    override val url = "http://japanesetest4you.com/jlpt-n2-grammar-list/"

    val local = Jt4y::class.java.classLoader.getResource("JLPTN2GrammarList–Japanesetest4you.html").readText()

    override fun get(): List<Lesson> = (if (fromFile) Jsoup.parse(local) else Jsoup.connect(url).get())
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
            .map { extractLesson(it) }


    fun extractLesson(lessonUrl: String) = Jsoup.connect(lessonUrl).get().let { doc ->
        val title = doc.getElementsByClass("title").first().text()
                .filterNot(Char::encodeable)

        val content = doc
                .getElementsByClass("entry").first()
                .getElementsByTag("p")
                .map(Element::text)
                .filter(String::isNotBlank) // filter out empty lines
                .map(String::removeIllegalChars) // replace problematic chars
                .filterNot { it.take(5).all(Char::encodeable) } // filter out lines that can be encoded with ANSI
                .drop(1) // drop the first line (form)
                .map(String::removeNonJap) // remove non Japanese parts from the remaining lines
                .map(String::fixEndOfSentence)
                .joinToString("")

        Lesson(
                language = "ja",
                title = title,
                text = content,
                collection = 274307,
                tags = listOf("JLPT", "Grammar"),
                url = lessonUrl
        )
    }


}