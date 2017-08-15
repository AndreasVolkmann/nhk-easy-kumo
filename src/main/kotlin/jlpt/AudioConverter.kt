package jlpt

import javazoom.jl.converter.Converter
import java.io.File
import java.io.SequenceInputStream
import java.net.URL
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

object AudioConverter {

    fun convert(name: String, url: String): String = URL(url).openConnection()
            .apply {
                setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                connect()
            }
            .getInputStream()
            .use {
                val filename = "$name.wav"
                Converter().convert(it, filename, null, null)
                filename
            }

    fun join(name1: String, name2: String) {
        val clip1 = AudioSystem.getAudioInputStream(File(name1))
        val clip2 = AudioSystem.getAudioInputStream(File(name2))

        val clips = listOf(clip1, clip2)

        val appendedFiles = AudioInputStream(
                SequenceInputStream(TODO()),
                clip1.format,
                clip1.frameLength + clip2.frameLength)

        // TODO multiple joins
        AudioInputStream(appendedFiles, clip1.format, appendedFiles.frameLength)

        AudioSystem.write(appendedFiles,
                AudioFileFormat.Type.WAVE,
                File("wavAppended.wav"))
    }

}

