package lingq

import data.Lesson
import org.junit.jupiter.api.Test

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


}