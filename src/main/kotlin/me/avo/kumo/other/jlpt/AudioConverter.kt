package me.avo.kumo.other.jlpt

import javazoom.jl.converter.Converter
import me.avo.kumo.util.getAudioInputStream
import me.avo.kumo.util.joinAudioStreams
import me.avo.kumo.util.writeAudio
import java.io.File
import java.net.URL
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream

object AudioConverter {

    fun convert(name: String, url: String): String = URL(url).openConnection()
        .apply {
            setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
            )
            connect()
        }
        .getInputStream()
        .use {
            val filename = "$name.wav"
            Converter().convert(it, filename, null, null)
            filename
        }

    fun join(name1: String, name2: String) {
        val clip1 = File(name1).getAudioInputStream()
        val clip2 = File(name2).getAudioInputStream()

        val appendedFiles = joinAudioStreams(clip1, clip2)

        // TODO multiple joins
        AudioInputStream(appendedFiles, clip1.format, appendedFiles.frameLength)

        writeAudio(appendedFiles, AudioFileFormat.Type.WAVE, File("wavAppended.wav"))
    }

}

