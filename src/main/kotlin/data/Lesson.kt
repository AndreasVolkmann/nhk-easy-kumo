package data

import org.bson.Document

/**
 * Created by Av on 4/22/2017.
 */
data class Lesson(
        val language: String,
        val title: String,
        val text: String,
        val share_status: String = "shared",
        val collection: Int,
        val level: String? = null, // DO NOT USE
        val external_audio: String? = null,
        val duration: Long? = null,
        val image: String? = null, // image url
        val tags: List<String>? = null,
        val url: String? = null
) {

    fun toDocument() = Document()
            .append("title", title)
            .append("text", text)
            .append("language", language)
            .append("share_status", share_status)
            .append("collection", collection)
            .append("tags", tags)
            .append("url", url)

}