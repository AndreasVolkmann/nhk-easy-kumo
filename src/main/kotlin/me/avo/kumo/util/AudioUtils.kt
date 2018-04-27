package me.avo.kumo.util

import java.io.File
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

fun File.getAudioInputStream(): AudioInputStream = AudioSystem.getAudioInputStream(this)

fun joinAudioStreams(one: AudioInputStream, two: AudioInputStream) = AudioInputStream(
    SequenceInputStream(one, two),
    one.format,
    one.frameLength + two.frameLength
)

fun writeAudio(audioInputStream: AudioInputStream, format: AudioFileFormat.Type, destination: File): File =
    destination.also {
        AudioSystem.write(
            audioInputStream,
            format,
            destination
        )
    }


fun joinAudioStreams(streams: Collection<AudioInputStream>): AudioInputStream {
    val vector = Vector<InputStream>(streams).elements()
    val format = streams.first().format
    return AudioInputStream(
        SequenceInputStream(vector),
        format,
        streams
            .map { it.frameLength }
            .reduce { total, length -> total + length }
    )
}