package me.avo.kumo.other.jlpt

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.UpdateResult
import me.avo.kumo.lingq.Lesson
import me.avo.kumo.util.Mongo

object JlptMongo : Mongo {

    override val database = "me/avo/kumo/other/jlpt"
    override val collection = "lessons"

    fun saveLesson(lesson: Lesson): UpdateResult = this {
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