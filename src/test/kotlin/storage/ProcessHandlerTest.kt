package storage

import isWindows
import mongoPath
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import path

internal class ProcessHandlerTest {

    @Test
    fun sound() = try {
        val pro = ProcessHandler.start()
        try {
            Thread.sleep(2000)
            ProcessHandler.isRunning() shouldEqualTo true
        } finally {
            pro?.destroy()
            Thread.sleep(1000)
            ProcessHandler.isRunning() shouldEqualTo false
        }
    }  catch (e: Error) {
        println("Windows: $isWindows")
        println(path)
        println(mongoPath)
        throw e
    }


}