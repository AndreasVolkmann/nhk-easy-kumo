package me.avo.kumo.nhk.pages

import me.avo.kumo.nhk.processing.audio.AudioParser
import org.junit.jupiter.api.Test
import java.io.File

internal class AudioParserTest {

    private val destinationDir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")

    @Test fun run() {

        //val id = "k10011409951000"
        val ffmpegPath = "D:\\Programme\\ffmpeg-20180411-9825f77-win64-static"
        AudioParser(destinationDir, destinationDir, ffmpegPath).let { ap ->

            val files = destinationDir.listFiles().filter { it.extension == "ts" }
            println("Found ${files.size} files")
            ap.demuxSegments(files)
                .let(ap::mergeAudio)

            //it.run()
        }

    }

}