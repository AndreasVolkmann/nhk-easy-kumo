package me.avo.kumo.nhk.data

import me.avo.kumo.lingq.*
import me.avo.kumo.util.*
import java.io.*

data class Article(
        val id: String,
        val url: String,
        val title: String,
        val date: String,
        val content: String,
        val image: ByteArray,
        val imageUrl: String? = null,
        val audio: ByteArray,
        val audioUrl: String,
        val dir: File,
        val tags: List<String>,
        val imported: Boolean = false
) {

    val imageFile = getImageFile(dir)

    val audioFile = getAudioFile(dir)

    val htmlFile = getHtmlFile(dir)

    fun toLesson() = Lesson(
            title = title,
            text = content,
            language = "ja",
            collection = 266730,
            external_audio = audioUrl,
            duration = audioFile.getDuration(),
            image = imageUrl,
            tags = listOf("NHK", "News") + tags,
            share_status = "shared"
    )


    companion object {

        fun getImageFile(dir: File) = File(dir.absolutePath + "/image.jpg")
        fun getAudioFile(dir: File) = File(dir.absolutePath + "/audio.mp3")
        fun getHtmlFile(dir: File) = File(dir.absolutePath + "/package.html")
    }

}