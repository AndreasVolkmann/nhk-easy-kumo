package storage

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import data.Lesson
import org.bson.Document

/**
 * Created by Av on 4/22/2017.
 */
object JlptMongo : Mongo {

    override val database = "jlpt"

    const val col = "lessons"

    operator fun <T> invoke(body: MongoCollection<Document>.() -> T) = this(col) {
        body()
    }

    fun saveLesson(lesson: Lesson) = this {
        replaceOne(eq("title", lesson.title), lesson.toDocument(), UpdateOptions().upsert(true))
    }

    fun loadLessons() = this {
        find().map {
            Lesson(
                    title = it.getString("title"),
                    text = it.getString("text"),
                    language = it.getString("language"),
                    share_status = it.getString("share_status"),
                    collection = it.getInteger("collection"),
                    tags = it["tags"] as List<String>,
                    url = it.getString("url")
            )
        }.toList()
    }

}