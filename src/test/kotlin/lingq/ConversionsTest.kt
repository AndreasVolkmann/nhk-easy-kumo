package lingq

import org.junit.jupiter.api.Test
import util.loadResource
import util.print

/**
 * Created by Av on 4/22/2017.
 */
internal class ConversionsTest {

    @Test
    fun extractTitle() {
        val json = this::class.loadResource("CourseResponse.json")

        val titles = resToTitle(json)

        titles.print(5)



    }


}