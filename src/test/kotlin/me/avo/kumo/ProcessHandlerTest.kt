package me.avo.kumo

import me.avo.kumo.util.ProcessHandler
import me.avo.kumo.util.isWindows
import me.avo.kumo.util.mongoPath
import me.avo.kumo.util.path
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test

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
    } catch (e: Error) {
        println("Windows: $isWindows")
        println(path)
        println(mongoPath)
        throw e
    }


}