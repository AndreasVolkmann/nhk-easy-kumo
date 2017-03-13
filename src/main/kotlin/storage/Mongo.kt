package storage

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.UpdateOptions
import data.Article
import data.Headline
import org.bson.Document
import org.bson.conversions.Bson

/**
 * Created by Av on 3/12/2017.
 */
object Mongo {

    const val host = "localhost"
    const val port = 27017
    const val database = "nhk"
    val collection = if (System.getProperty("ENV") == "TEST") "test" else "articles"

    fun connect() = MongoClient(host, port)

    operator fun <T> invoke(body: MongoCollection<Document>.() -> T) = connect().use {
        val col = it.getDatabase(database).getCollection(collection)
        body(col)
    }


    fun saveArticles(vararg articles: Article) = saveArticles(articles.toList())

    fun saveArticles(articles: List<Article>) = Mongo {
        val docs = articles.map(Article::toDocument)
        docs.forEach {
            val id = it["id"].toString()
            findOneAndReplace(eq("id", id), it, FindOneAndReplaceOptions().upsert(true))
        }
    }

    fun updateArticle(article: Article): Document? = Mongo {
        findOneAndUpdate(eq("id", article.id), Document("\$set", Document("imported", true)), FindOneAndUpdateOptions().upsert(true))
    }

    fun loadArticles(): List<Article> = Mongo {
        find().toList().map(Document::toArticle)
    }

    fun filterImported(articles: List<Article>) = Mongo {
        articles.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0 // no articles with this id found
        }
    }

    fun filterHeadlines(headlines: List<Headline>) = Mongo {
        headlines.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0
        }
    }

    fun byId(id: String): Bson = eq("id", id)

}