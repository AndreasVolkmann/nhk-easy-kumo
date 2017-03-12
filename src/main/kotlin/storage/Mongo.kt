package storage

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import data.Article
import org.bson.Document

/**
 * Created by Av on 3/12/2017.
 */
object Mongo {

    const val host = "localhost"
    const val port = 27017
    const val database = "nhk"
    const val collection = "articles"

    fun connect() = MongoClient(host, port)

    operator fun <T> invoke(body: MongoCollection<Document>.() -> T) = connect().use {
        val col = it.getDatabase(database).getCollection(collection)
        body(col)
    }


    fun saveArticles(vararg articles: Article) = saveArticles(articles.toList())

    fun saveArticles(articles: List<Article>) = Mongo {
        val docs = articles.map(Article::toDocument)
        insertMany(docs)
    }

    fun updateArticle(id: String): Document? = Mongo {
        findOneAndUpdate(eq("id", id), Document("\$set", Document("imported", true)), FindOneAndUpdateOptions().upsert(true))
    }

    fun loadArticles(): List<Article> = Mongo {
        find().toList().map(Document::toArticle)
    }

    fun filterImported(articles: List<Article>) = Mongo {
        articles.filter {
            //find(eq("id", it.id))
            false
        }
    }

}