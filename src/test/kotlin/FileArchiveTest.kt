import org.amshove.kluent.shouldBeGreaterThan
import org.junit.jupiter.api.Test

/**
 * Created by Av on 3/13/2017.
 */
internal class FileArchiveTest {

    @Test
    fun readarchive() {

        val headlines = FileArchive.read()

        headlines.forEach(::println)
        headlines.size shouldBeGreaterThan 1

    }


    @Test
    fun reload() {


        Crawler.reload()


    }

}