package storage

import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test

/**
 * Created by Av on 3/27/2017.
 */
internal class ProcessTest {

    @Test
    fun testRunning() {
        val res = Process.isRunning()
        println(res)
    }

    @Test
    fun sound() {
        val pro = Process.start()
        try {
            Thread.sleep(2000)
            Process.isRunning() shouldEqualTo true
        } finally {
            pro?.destroy()
            Thread.sleep(1000)
            Process.isRunning() shouldEqualTo false
        }

    }


}