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
        val dir: File,
        val imported: Boolean = false
) {

    val imageFile = getImageFile(dir)

    val audioFile = getAudioFile(dir)

    val htmlFile = getHtmlFile(dir)


    companion object {

        fun getImageFile(dir: File) = File(dir.absolutePath + "/image.jpg")
        fun getAudioFile(dir: File) = File(dir.absolutePath + "/audio.mp3")
        fun getHtmlFile(dir: File) = File(dir.absolutePath + "/package.html")
    }

}