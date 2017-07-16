package storage

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import data.Article
import data.Headline
import org.bson.Document
import org.bson.conversions.Bson

object NhkMongo : Mongo {

    override val database = "nhk"
    val collection = if (System.getProperty("ENV") == "TEST") "test" else "articles"

    operator fun <T> invoke(body: MongoCollection<Document>.() -> T) = this(collection) {
        body()
    }

    fun saveArticles(vararg articles: Article) = saveArticles(articles.toList())

    fun saveArticles(articles: List<Article>) = NhkMongo {
        val docs = articles.map(Article::toDocument)
        docs.forEach {
            val id = it["id"].toString()
            findOneAndReplace(eq("id", id), it, FindOneAndReplaceOptions().upsert(true))
        }
    }

    fun updateArticle(article: Article): Document? = NhkMongo {
        findOneAndUpdate(eq("id", article.id), Document("\$set", Document("imported", true)), FindOneAndUpdateOptions().upsert(true))
    }

    fun loadArticles(): List<Article> = NhkMongo {
        find().toList().map(Document::toArticle)
    }

    fun filterImported(articles: List<Article>) = NhkMongo {
        articles.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0 // no articles with this id found
        }
    }

    fun filterHeadlines(headlines: List<Headline>) = NhkMongo {
        headlines.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0
        }
    }

    fun byId(id: String): Bson = eq("id", id)

}