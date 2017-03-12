import data.Article
import lingq.Lingq
import org.junit.jupiter.api.Test
import java.io.File


class LingqTest {

    companion object {
        val path = "V:\\NHK-Easy\\articles\\2017-03-10\\k10010903831000"
        val article = Article("1", "test.com", "Test Article", "2017-03-11", "This is just a test!", ByteArray(0), ByteArray(0), "audio.com", File(path))
    }

    @Test
    fun connect() {
        val lingq = Lingq(article)

        lingq.import()


    }

}