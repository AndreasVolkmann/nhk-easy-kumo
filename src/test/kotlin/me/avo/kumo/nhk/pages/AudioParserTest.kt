package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.processing.audio.AudioParser
import org.junit.jupiter.api.Test
import java.io.File

internal class AudioParserTest {

    @Test fun run() {

        AudioParser("k10011409951000").let {




            fun runDemux() {
                val dir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")
                val files = dir.listFiles().filter { it.extension == "ts" }

                files.forEach { f ->
                    println(f.name)
                    it.demux(f, dir)
                }
            }

            runDemux()

            //it.run()
        }

    }

}