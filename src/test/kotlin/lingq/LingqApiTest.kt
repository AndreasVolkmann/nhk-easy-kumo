package lingq

import data.Lesson
import jlpt.JlptManager
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
                share_status = "shared",
                level = "1"
        )

        LingqApi.postLesson(test)
    }

    val regex = Regex("[a-zA-Z]")
    fun getLessons() = LingqApi.getLessons(language = "ja", collection = 274307)


    @Test
    fun postLessons() = JlptManager.postLessons(5)

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