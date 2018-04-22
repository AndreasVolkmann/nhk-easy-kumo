package me.avo.kumo.util

import java.io.File
import java.io.SequenceInputStream
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
