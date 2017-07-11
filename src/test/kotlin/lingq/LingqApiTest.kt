package lingq

import data.Lesson
import jlpt.JlptManager
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import storage.JlptMongo


internal class LingqApiTest {

    @Test
    fun postArticle() {
        val test = Lesson(
                title = "あるいは",
                text = "This is just a TEst",
                collection = 274588,
                language = "ja",
                share_status = "shared",
                level = "1",
                image = "http://www3.nhk.or.jp/news/html/20170423/K10010958581_1704232057_1704232059_01_03.jpg"
        )

        LingqApi.postLesson(test)
    }

    val regex = Regex("[a-zA-Z]")
    fun getLessons() = LingqApi.getLessons(language = "ja", collection = 274307)


    @Test
    fun postLessons() = JlptManager.postLessons(10)

    @Test
    fun postFixed() {
        val importedTitles = getLessons()
        importedTitles.shouldNotBeEmpty()
        importedTitles.take(5).forEach { println(it) }

        JlptMongo.loadLessons()
                .filter { it.text.isNotBlank() }
                .filterNot { it.title in importedTitles }
                .filterNot { it.text.contains(regex) }
                .onEach { println(it) }

    }


}