package lingq

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import data.Lesson
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import util.PropertyReader
import util.getLogger
import java.nio.charset.Charset

/**
 * Created by Av on 4/21/2017.
 */
object LingqApi {

    val logger = this::class.getLogger()

    val url = "https://www.lingq.com/api/v2/ja/lessons/"
    val key = PropertyReader.getProperty("lingq.key")

    fun getClient() = HttpClientBuilder.create()
            .setDefaultHeaders(mutableListOf(BasicHeader("Authorization", "Token $key")))
            .build()


    fun postLesson(lesson: Lesson) = getClient().use { client ->
        val content = with(lesson) {
            jsonObject(
                    "title" to title,
                    "text" to text,
                    "share_status" to share_status,
                    "collection" to collection
            ).apply {
                if (level != null) addProperty("level", level)
                if (external_audio != null) addProperty("external_audio", external_audio)
                if (duration != null) addProperty("duration", duration)
                //if (image != null) addProperty("image" ,image) TODO support for image data
                if (tags != null) add("tags", jsonArray(*tags.toTypedArray()))
            }
        }.toString()

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

    fun formatResponse(response: CloseableHttpResponse) = EntityUtils.toString(response.entity, Charset.forName("UTF-8"))

}