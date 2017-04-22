package jlpt

import data.Lesson
import org.jsoup.Jsoup
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

    val encoder = Charsets.ISO_8859_1.newEncoder()!!

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
                .filterNot { encoder.canEncode(it) }

        val content = doc
                .getElementsByClass("entry").first()
                .getElementsByTag("p")
                .map { it.text() }
                .filter { it.isNotBlank() } // filter out empty lines
                .map { it.replace("’", "") } // replace problematic chars
                .filterNot { it.take(5).all { encoder.canEncode(it) } } // filter out lines that can be encoded with ANSI
                .drop(1) // drop the first line (form)
                .map { it.dropLastWhile { encoder.canEncode(it) } } // remove non Japanese parts from the remaining lines
                .joinToString("\r\n")

        Lesson(
                language = "ja",
                title = title,
                text = content,
                collection = 274307,
                tags = listOf("JLPT", "Grammar")
        )
    }


}