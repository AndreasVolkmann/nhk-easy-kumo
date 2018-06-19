package me.avo.kumo.nhk.pages

import me.avo.getResourceAsFile
import me.avo.kumo.nhk.processing.audio.AudioParser
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File

@Tag("local")
internal class AudioParserTest {

    private val destinationDir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")
    private val ffmpegPath = "D:\\Programme\\ffmpeg-20180411-9825f77-win64-static"

    private val parser = AudioParser(destinationDir, destinationDir, ffmpegPath)

    @Disabled
    @Test fun run() {
        //val id = "k10011409951000"
        parser.let { ap ->

            val files = destinationDir.listFiles().filter { it.extension == "ts" }
            println("Found ${files.size} files")
            ap.demuxSegments(files)
                .let(ap::mergeAlt)

            //it.run()
        }
    }

    @Test fun merge() {
        val streamOne = getResourceAsFile("segment1_0_a.wav")
        val streamTwo = getResourceAsFile("segment2_0_a.wav")
        val streams = listOf(streamOne, streamTwo)

        parser.mergeAlt(streams)
    }

}