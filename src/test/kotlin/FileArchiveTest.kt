import org.amshove.kluent.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import storage.FileArchive

internal class FileArchiveTest {

    @Test
    fun readarchive() {

        val headlines = FileArchive.read()

        headlines.forEach(::println)
        headlines.size shouldBeGreaterThan 1

    }

}