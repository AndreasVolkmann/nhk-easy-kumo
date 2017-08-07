package jlpt

import data.Lesson
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Jt4yLesson(val resource: Resource) {

    fun extract() = resource.load().let { doc ->
        val title = doc.getElementsByClass("title").first().text()
                .filterNot(Char::encodeable)
        val content = extractContent(doc)

        Lesson(language = "ja",
                title = title,
                text = content,
                collection = 274307,
                tags = listOf("JLPT", "Grammar"),
                url = resource.url,
                external_audio = "http://www.freesfx.co.uk/rx2/mp3s/6/18660_1464810669.mp3",
                duration = 1)
    }

    fun extractAudio(document: Document) = document
            .getElementsByTag("audio")
            .map { it.attr("src") }

    fun extractContent(document: Document) = document
            .getElementsByClass("entry").first()
            .getElementsByTag("p")
            .map(Element::text)
            .filter(String::isNotBlank) // filter out empty lines
            .joinToString("|")
            .substringAfter("Example sentences:")
            .split("|")
            .map(String::removeIllegalChars) // replace problematic chars
            .filterNot { it.take(5).all(Char::encodeable) } // filter out lines that can be encoded with ANSI
            //.drop(1) // drop the first line (form)
            .map(String::removeNonJap) // remove non Japanese parts from the remaining lines
            .map(String::fixEndOfSentence)
            .joinToString("")

}