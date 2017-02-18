package data

import Application
import java.io.File

data class Article(
        val id: String,
        val url: String,
        val title: String,
        val date: String,
        val content: String,
        val image: ByteArray,
        val audio: ByteArray,
        val audioUrl: String
) {

    val folder get() = File("${Application.fileDir}/${this.date}/$id")

}