package storage

import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test

/**
 * Created by Av on 3/27/2017.
 */
internal class ProcessHandlerTest {

    @Test
    fun testRunning() {
        val res = ProcessHandler.isRunning()
        println(res)
    }

    @Test
    fun sound() {
        val pro = ProcessHandler.start()
        try {
            Thread.sleep(2000)
            ProcessHandler.isRunning() shouldEqualTo true
        } finally {
            pro?.destroy()
            Thread.sleep(1000)
            ProcessHandler.isRunning() shouldEqualTo false
        }

    }


}