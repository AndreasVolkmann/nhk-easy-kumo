package lingq

import data.Lesson
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import storage.JlptMongo

/**
 * Created by Av on 4/21/2017.
 */
internal class LingqApiTest {

    @Test
    fun postArticle() {
        val test = Lesson(
                title = "あるいは",
                text = "This is just a TEst",
                collection = 274307,
                language = "ja",
                share_status = "private"
        )

        LingqApi.postLesson(test)
    }

    @Test
    fun getLessons() {
        val lessons = LingqApi.getLessons(language = "ja", collection = 274307)
        println(lessons)

    }

    @Test
    fun postFixed() {
        val importedTitles = LingqApi.getLessons(language = "ja", collection = 274307)
        importedTitles.shouldNotBeEmpty()
        importedTitles.take(5).forEach { println(it) }

        val regex = Regex("[a-zA-Z]")
        JlptMongo.loadLessons()
                .filter { it.text.isNotBlank() }
                .filterNot { it.title in importedTitles }
                .filterNot { it.text.contains(regex) }
                .onEach { println(it) }

    }


}