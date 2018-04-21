package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.processing.audio.AudioParser
import org.junit.jupiter.api.Test
import java.io.File

internal class AudioParserTest {

    @Test fun run() {

        AudioParser("k10011409951000").let {

            //it.run()

            fun runDemux() {
                val dir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")
                val files = dir.listFiles()

                files.forEach { f ->
                    println(f.name)
                    it.demux(f)
                }
            }

            runDemux()

        }

    }

}