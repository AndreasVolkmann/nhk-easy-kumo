package lingq

import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJsonArray
import data.Lesson
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import util.PropertyReader
import util.getLogger
import java.nio.charset.Charset

object LingqApi {

    val url = "https://www.lingq.com/api/v2/ja/lessons/"
    val key = PropertyReader.getProperty("lingq.key")

    fun getClient(): CloseableHttpClient = HttpClientBuilder.create()
            .setDefaultHeaders(mutableListOf(BasicHeader("Authorization", "Token $key")))
            .build()

    fun postLesson(lesson: Lesson) = getClient().use { client ->
        val content = lesson.toJson()

        val req = HttpPost(url).apply {
            entity = StringEntity(content, HTTP.UTF_8).apply {
                contentType = BasicHeader("Content-type", "application/json; charset=utf-8")
            }
            println(entity.content.bufferedReader().use { it.readText() })
        }

        val res = client.execute(req)
        handleResponse(url, res)
        formatResponse(res)
    }

    fun Lesson.toJson() = jsonObject(
            "title" to title,
            "text" to text,
            "share_status" to share_status,
            "collection" to collection
    ).apply {
        level?.let { addProperty("level", level) }
        external_audio?.let { addProperty("external_audio", external_audio) }
        duration?.let { addProperty("duration", duration) }
        image?.let { addProperty("image", image) }
        tags?.let { add("tags", tags.toJsonArray()) }
    }.toString()

    fun getLessons(language: String, collection: Int) = getClient().use {
        val finalUrl = "https://www.lingq.com/api/languages/$language/course/?course=$collection" // alternate
        logger.trace("Get Lessons: $finalUrl")
        val req = HttpGet(finalUrl)
        val res = it.execute(req)
        handleResponse(finalUrl, res)
        resToTitle(formatResponse(res))
    }

    fun handleResponse(url: String, response: CloseableHttpResponse) = if (response.statusLine.statusCode > 201) {
        println(response.entity.content.bufferedReader().use { it.readText() })
        println(url)
        throw RuntimeException("Failed : HTTP error code : " + response.statusLine.statusCode)
    } else Unit

    fun formatResponse(response: CloseableHttpResponse): String = EntityUtils
            .toString(response.entity, Charset.forName("UTF-8"))

    private val logger = this::class.getLogger()

}