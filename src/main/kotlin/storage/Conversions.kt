package storage

import data.Article
import org.bson.Document
import java.io.File

fun Article.toDocument(): Document = Document()
        .append("id", id)
        .append("url", url)
        .append("title", title)
        .append("date", date)
        .append("content", content)
        .append("audioUrl", audioUrl)
        .append("imported", imported)

fun Document.toArticle(): Article = Article(
        id = get("id").toString(),
        url = get("url").toString(),
        title = get("title").toString(),
        date = get("date").toString(),
        content = get("content").toString(),
        image = ByteArray(0),
        audio = ByteArray(0),
        audioUrl = get("audioUrl").toString(),
        dir = File("")
)