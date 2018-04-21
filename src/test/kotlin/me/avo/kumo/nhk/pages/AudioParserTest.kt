package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.processing.audio.AudioParser
import org.junit.jupiter.api.Test
import java.io.File

internal class AudioParserTest {

    @Test fun run() {

        val destinationDir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")
        val ffmpegPath = "D:\\Programme\\ffmpeg-20180411-9825f77-win64-static"
        AudioParser("k10011409951000", destinationDir, destinationDir, ffmpegPath).let { ap ->

            val files = destinationDir.listFiles().filter { it.extension == "ts" }
            println("Found ${files.size} files")
            ap.demuxSegments(files)
                .let(ap::mergeAudio)

            //it.run()
        }

    }

}