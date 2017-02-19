package data

import java.io.File

data class Article(
        val id: String,
        val url: String,
        val title: String,
        val date: String,
        val content: String,
        val image: ByteArray,
        val audio: ByteArray,
        val audioUrl: String,
        val dir: File
) {

    val imageFile = getImageFile(dir)

    val audioFile = getAudioFile(dir)

    val contentFile = getContentFile(dir)

    val finalContent = title + "\n" +
            url + "\n" +
            audioUrl + "\n" +
            content

    companion object {

        fun getImageFile(dir: File) = File(dir.absolutePath + "/image.jpg")
        fun getAudioFile(dir: File) = File(dir.absolutePath + "/audio.mp3")
        fun getContentFile(dir: File) = File(dir.absolutePath + "/content.txt")

    }

}